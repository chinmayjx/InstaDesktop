# %%
import os
import json
import random
import numpy as np
from PIL import ImageFilter, Image
from numpy.core.fromnumeric import mean, sort


# %%
with open('data/rgb.json','r') as f:
    rgb = json.load(f)
with open('data/dc.json','r') as f:
    dc = json.load(f)



# %%
all = os.listdir('img')
max_d = 30
r2 = random.randint(0,len(all)-1)
i2 = Image.open('img/'+all[r2])
# i2.show()

lst = dc[all[r2]]
endi = len(lst)
for i in range(len(lst)):
    if lst[i][1] > max_d:
        endi = i
        break

r1 = int(np.random.exponential(endi/2))%endi
i1 = Image.open('img/'+lst[r1][0])
print(endi, r1, lst[r1])



i2 = i2.resize((int(i2.size[0]*1080/i2.size[1]),1080))
i1 = i1.resize((1920,1080))
i1 = i1.filter(ImageFilter.GaussianBlur(radius=45))
i1.paste(i2,(int((1920-i2.size[0])/2),0))
# i1.show()
i1.save('wallp.png')

os.system(f'gsettings set org.gnome.desktop.background picture-uri file:////{os.getcwd()+"/wallp.png"}')
