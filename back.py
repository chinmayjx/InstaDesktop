# %%
import re
import os
import sys
import json
import time
import pickle
import random
import urllib.request
from selenium import webdriver
from PIL import Image, ImageFilter
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.firefox import options
from selenium.webdriver.firefox.options import Options

driver = None
username = 'chinmayjain08'

def initialize_driver():
    global driver
    opt = Options()
    # opt.headless = True
    driver = webdriver.Firefox('firefox-driver',options=opt)
    
    driver.get('https://instagram.com')
    with open('data/cookies.pkl', 'rb') as f:
        cookies = pickle.load(f)
        for c in cookies:
            driver.add_cookie(c)

    driver.get(f'https://instagram.com/')


def save_login():
    with open('data/cookies.pkl', 'wb+') as f:
        pickle.dump(driver.get_cookies(), f)


def download_latest():
    driver.get(f'https://www.instagram.com/{username}/saved/')
    x = wait_for_element("//div[@class='v1Nh3 kIKUG  _bz0w']")
    x.click()
    download_inseq()


TIMEOUT = 5
TIME_PERIOD = 0.3


def wait_for_element(xpath: str, to=TIMEOUT, tp=TIME_PERIOD):
    element = None
    lt = time.time()
    while element == None:
        try:
            element = driver.find_element_by_xpath(xpath)
        except:
            pass
        time.sleep(tp)
        ct = time.time()
        if ct-lt > to:
            print("can't find : ", xpath)
            break
    return element


def wait_for_elements(xpath: str, to=TIMEOUT, tp=TIME_PERIOD):
    elements = []
    lt = time.time()
    while len(elements) == 0:
        elements = driver.find_elements_by_xpath(xpath)
        time.sleep(tp)
        ct = time.time()
        if ct-lt > to:
            print("can't find : ", xpath)
            break
    return elements

# %%
downloaded = {}
def download_inseq():
    try:
        nb = wait_for_element(
            "//a[@class=' _65Bje    coreSpriteRightPaginationArrow']")
        limg = []
        ct = 0
        for itrt in range(100000):
            img = wait_for_elements(
                "//div[not(@class='eLAPa')]/div[@class='KL4Bh']/img")
            if len(img) == 3 and img == limg: continue
            tmpl = driver.current_url.split('/')
            while len(tmpl[-1])==0 : del tmpl[-1]
            post_id = tmpl[-1]

            if post_id not in downloaded : downloaded[post_id] = []
            try:
                rb = driver.find_element_by_xpath(
                    "//div[@class='    coreSpriteRightChevron  ']")
                rbe = True
            except:
                rbe = False

            try:
                lb = driver.find_element_by_xpath(
                    "//div[@class='   coreSpriteLeftChevron   ']")
                lbe = True
            except:
                lbe = False
            if (not lbe and not rbe) or (rbe and not lbe) : dwn = 0
            else : dwn = 1

            if len(img) > 0:
                fnm = "img/a"+str(ct)+".jpg"
                urllib.request.urlretrieve(img[dwn].get_attribute(
                    'srcset').split(',')[-1].split()[0], fnm)
                downloaded[post_id].append(fnm)
                
            else:
                print('noimg')
            try:
                rb.click()
                time.sleep(0.1)
            except:
                nb.click()
                time.sleep(0.1)

            limg = img
            ct += 1
    except:
        pass
    with open('data/downloaded.json','w+') as f:
        json.dump(downloaded,f)
# %%
if __name__ == '__main__':
    initialize_driver()
