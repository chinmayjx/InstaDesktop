# %%
from PIL import ImageFilter, Image, ImageEnhance, ImageDraw
from numpy import ceil
from scipy.sparse.construct import rand
from sklearn.cluster import KMeans
import random
import json
import os

# %%
all = os.listdir('img')
max_d = 30
r1 = random.randint(0, len(all)-1)
i1 = Image.open('img/'+all[r1])
i1.show()
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


clrs = topxclrs(img=i1,ncl=3,show=True)

# %%
i1 = i1.resize((int(i1.size[0]*1080/i1.size[1]),1080))
clrs = [tuple([int(x) for x in clr]) for clr in clrs]
empw = int((1920-i1.size[0])/2)

# %%

# cntr = 15
# for cnt in range(cntr):
#     l = random.randint(200,300)
#     x = random.randint(-l,empw+l)
#     y = random.randint(-l,bg.size[1])
    
#     draw.ellipse((x, y, x+l, y+l), fill = clrs[random.randint(0,len(clrs)-1)])

# for cnt in range(cntr):
#     l = random.randint(200,300)
#     x = random.randint(bg.size[0]-empw-l,bg.size[0]+l)
#     y = random.randint(-l,bg.size[1])
    
#     draw.ellipse((x, y, x+l, y+l), fill = clrs[random.randint(0,len(clrs)-1)])

# bg.show()




# %%
bg = Image.new("RGB",(1920,1080),color=tuple([0]*3))
draw = ImageDraw.Draw(bg)
def make_circles(draw: ImageDraw.Draw, region: tuple, diar: tuple, pc: float):
    x,y = region[0][0],region[0][1]
    w = region[1][0] - region[0][0]
    h = region[1][1] - region[0][1]
    jmp = int((diar[0] + diar[1])/2)
    

    for i in range(x,x+w+jmp+1,jmp):
        for j in range(y,y+h+jmp+1,jmp):
            if random.random() < pc:
                rds = int(random.randint(diar[0],diar[1])/2)
                draw.ellipse((i-rds, j-rds, i+rds, j+rds), fill = clrs[random.randint(0,len(clrs)-1)])

diar = (100,150)
pc = 0.25
print(pc)
make_circles(draw,((0,0),(empw,bg.size[1])),diar,pc)
make_circles(draw,((bg.size[0]-empw,0),(bg.size[0],bg.size[1])),diar,pc)
bg.show()



bg = bg.filter(ImageFilter.GaussianBlur(radius=120))
bg = ImageEnhance.Brightness(bg).enhance(0.8)
# bg = ImageEnhance.Contrast(bg).enhance(0.7)
bg = ImageEnhance.Color(bg).enhance(1.5)
bg.paste(i1,(empw,0))
bg.show()
# %%

def build_wallp(img: Image.Image):
    pass