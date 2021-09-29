# %%
from PIL import ImageFilter, Image, ImageEnhance
from sklearn.cluster import KMeans
import random
import json
import os


all = os.listdir('img')
max_d = 30
r1 = random.randint(0, len(all)-1)
i1 = Image.open('img/'+all[r1])
i1.show()
def top5clrs(i1: Image.Image, show=False):
    w, h = 150, 150
    i1 = i1.resize((w, h))
    clrs = []
    for x in range(0, w):
        for y in range(0, h):
            clrs.append(i1.getpixel((x, y)))

    kmeans = KMeans(n_clusters=5)
    kmeans.fit(clrs)
    if show:
        cntrs = kmeans.cluster_centers_
        clri = Image.new("RGB", (1000, 200))
        for i in range(5):
            clri.paste(Image.new("RGB", (200, 200), color=tuple(
                [int(x) for x in cntrs[i]])), (200*i, 0))
        clri.show()
    return kmeans.cluster_centers_


print(top5clrs(i1,show=True))

