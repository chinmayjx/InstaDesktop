# %%
import os
import time
import random
import urllib.request
from selenium import webdriver
from PIL import Image, ImageFilter
from selenium.webdriver.common.keys import Keys


# %%
username = None
passwd = None
driver = webdriver.Firefox('firefox-driver')
driver.get('https://instagram.com')
time.sleep(5)
driver.find_element_by_name('username').send_keys(username)
driver.find_element_by_name('password').send_keys(passwd)
time.sleep(1)
driver.find_element_by_xpath("//button[@class='sqdOP  L3NKy   y3zKF     ']").click()
time.sleep(5)
driver.get(f'https://www.instagram.com/{username}/saved/')

# %%
nb = driver.find_elements_by_xpath("//a[@class=' _65Bje    coreSpriteRightPaginationArrow']")
print(len(nb))
ct = 0
while True:
    rb = driver.find_elements_by_xpath("//div[@class='    coreSpriteRightChevron  ']")
    print(len(rb))
    try:
        while True:
            ct += 1
            img = driver.find_elements_by_xpath("//div[not(@class='eLAPa')]/div[@class='KL4Bh']//img")
            if len(img)>0:
                dwn = 1
                if len(img) == 1: dwn = 0
                if len(img) == 2: dwn = 1-len(driver.find_elements_by_xpath("//div[@class='    coreSpriteRightChevron  ']"))
                urllib.request.urlretrieve(img[dwn].get_attribute('srcset').split(',')[-1].split()[0],"img/a"+str(ct)+".jpg")
            else:
                print('noimg')
            try:
                rb[0].click()
                time.sleep(1)
            except:
                break
        nb[0].click()
        time.sleep(3)
    except:
        break

# %%

img = driver.find_elements_by_xpath("//div[not(@class='eLAPa')]/div[@class='KL4Bh']//img")
print(len(img))
dwn = 1
if len(img) == 1: dwn = 0
if len(img) == 2: dwn = 1-len(driver.find_elements_by_xpath("//div[@class='    coreSpriteRightChevron  ']"))
urllib.request.urlretrieve(img[dwn].get_attribute('srcset').split(',')[-1].split()[0],"img/a"+".jpg")

# %%

ct = 0
for i in img:
    ct+=1
    urllib.request.urlretrieve(i.get_attribute('srcset').split(',')[-1].split()[0],"img/a"+str(ct)+".jpg")

    
# %%

links = img[0].get_attribute('srcset').split(',')
print(links)

# %%
ct = 0
for i in links:
    ct+=1
    urllib.request.urlretrieve(i.split()[0],"img/a"+str(ct)+".jpg")
    print(i.split()[0])
# %%
urllib.request.urlretrieve(img[0].get_attribute('srcset').split(',')[-1].split()[0],'a.jpg')


# %%
