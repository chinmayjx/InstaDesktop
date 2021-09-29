# %%
import os
import json
from math import sqrt
from PIL import Image
from numpy.core.fromnumeric import mean, sort

# %% export distance data

N_PARTS = 10

def pixav(img):
    w,h = img.size
    rr = gg = bb = 0
    ct = 0
    for x in range(0,w,int(w/N_PARTS)):
        for y in range(0,h,int(h/N_PARTS)):
            ct += 1
            rrt,ggt,bbt = img.getpixel((x,y))
            rr += rrt
            gg += ggt
            bb += bbt
    return (rr/ct,gg/ct,bb/ct)


all = os.listdir('img')
imgs = [None] * len(all)
dc = {}
rgb = {}

for i in range(len(all)):
    imgs[i] = Image.open('img/'+all[i])
    dc[all[i]] = []
    rgb[all[i]] = pixav(imgs[i])

# %%
with open('data/rgb.json','w+') as f:
    json.dump(rgb,f)

# %% 
for i in range(len(all)): dc[all[i]] = []

for i in range(len(all)):
    dc[all[i]].append((all[i],0))
    for j in range(i+1,len(all)):
        dst = sqrt((rgb[all[i]][0]-rgb[all[j]][0])*(rgb[all[i]][0]-rgb[all[j]][0])+(rgb[all[i]][1]-rgb[all[j]][1])*(rgb[all[i]][1]-rgb[all[j]][1])+(rgb[all[i]][2]-rgb[all[j]][2])*(rgb[all[i]][2]-rgb[all[j]][2]))
        dc[all[i]].append((all[j],dst))
        dc[all[j]].append((all[i],dst))
    
    dc[all[i]] = sorted(dc[all[i]],key=lambda t: t[-1])
# %%

with open('data/dc.json','w+') as f:
    json.dump(dc,f)
