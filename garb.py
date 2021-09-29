
def pixav(img):
    w,h = img.size
    rr = gg = bb = 0
    ct = 0
    for x in range(0,w,int(w/10)):
        for y in range(0,h,int(h/10)):
            ct += 1
            rrt,ggt,bbt = img.getpixel((x,y))
            rr += rrt
            gg += ggt
            bb += bbt
    return (rr/ct,gg/ct,bb/ct)

b1 = 999999999
b2 = 999999999
ori = pixav(i2)

for i in range(len(all)):
    if i == r2 : continue
    tmp = Image.open('img/'+all[i])
    tmpv = pixav(tmp)
    dst = sqrt((tmpv[0]-ori[0])*(tmpv[0]-ori[0])+(tmpv[1]-ori[1])*(tmpv[1]-ori[1])+(tmpv[2]-ori[2])*(tmpv[2]-ori[2]))
    if dst < b1:
        i3 = i1
        b2 = b1
        i1 = tmp
        b1 = dst
    elif dst < b2:
        i3 = tmp
        b2 = dst
