# %%
from PIL import ImageFilter, Image, ImageEnhance, ImageDraw
from numpy import ceil
from scipy.sparse.construct import rand
from sklearn.cluster import KMeans
import random
import json
import os

def topxclrs(img: Image.Image, ncl: int, show=False):
    w, h = 150, 150
    img = img.resize((w, h))
    clrs = []
    for x in range(0, w):
        for y in range(0, h):
            clrs.append(img.getpixel((x, y)))

    kmeans = KMeans(n_clusters=ncl)
    kmeans.fit(clrs)
    if show:
        cntrs = kmeans.cluster_centers_
        clri = Image.new("RGB", (200*ncl, 200))
        for i in range(ncl):
            clri.paste(Image.new("RGB", (200, 200), color=tuple(
                [int(x) for x in cntrs[i]])), (200*i, 0))
        clri.show()
    return kmeans.cluster_centers_


tc = {}
def export_data():
    all = os.listdir('img')
    for i in all:
        if i not in tc: tc[i]={}
        img = Image.open('img/'+i)
        clrs = topxclrs(img,3)
        clrs = [tuple([int(x) for x in clr]) for clr in clrs]
        tc[i]["t3clr"] = clrs
        
    with open('data/properties.json','w+') as f:
        json.dump(tc,f)
    

export_data()