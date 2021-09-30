# %%
from PIL import ImageFilter, Image, ImageEnhance, ImageDraw
from numpy import ceil
from scipy.sparse.construct import rand
from sklearn.cluster import KMeans
import random
import json
import os



def make_circles(draw: ImageDraw.Draw, clrs: list, region: tuple, diar: tuple, pc: float):
    x, y = region[0][0], region[0][1]
    w = region[1][0] - region[0][0]
    h = region[1][1] - region[0][1]
    jmp = int((diar[0] + diar[1])/2)

    for i in range(x, x+w+jmp+1, jmp):
        for j in range(y, y+h+jmp+1, jmp):
            if random.random() < pc:
                rds = int(random.randint(diar[0], diar[1])/2)
                draw.ellipse((i-rds, j-rds, i+rds, j+rds),
                             fill=clrs[random.randint(0, len(clrs)-1)])


def build_wallp(fnm: str):
    clrs = [tuple(li) for li in properties[fnm]["t3clr"]]
    img = Image.open('img/'+fnm)
    img = img.resize((int(img.size[0]*1080/img.size[1]), 1080))
    empw = int((1920-img.size[0])/2)
    # img.show()
    bg = Image.new("RGB", (1920, 1080), color=tuple([0]*3))
    draw = ImageDraw.Draw(bg)

    diar = (75, 150)
    pc = 0.25
    make_circles(draw, clrs, ((0, 0), (empw, bg.size[1])), diar, pc)
    make_circles(
        draw, clrs, ((bg.size[0]-empw, 0), (bg.size[0], bg.size[1])), diar, pc)
    # bg.show()

    bg = bg.filter(ImageFilter.GaussianBlur(radius=120))
    bg = ImageEnhance.Brightness(bg).enhance(0.7)
    # bg = ImageEnhance.Color(bg).enhance(1.5)
    bg.paste(img, (empw, 0))
    # bg.show()
    bg.save('wallp.png')
    os.system(
        f'gsettings set org.gnome.desktop.background picture-uri file:////{os.getcwd()+"/wallp.png"}')


with open('data/properties.json') as f:
    properties = json.load(f)

all = os.listdir('img')
fnm = random.choice(all)
build_wallp(fnm)
