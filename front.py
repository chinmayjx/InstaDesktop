# %%
from ttkbootstrap import Style
import tkinter as tk
import back


import importlib
importlib.reload(back)

root = tk.Tk()
root.title("InstaDesk")
root.geometry('600x350+1320+0')
Style().theme_use('darkly')

frame = tk.Frame(root)
frame.pack()


def mk_btn(txt, cmd):
    btn = tk.Button(frame, text=txt, command=cmd)
    btn.config(width=30)
    btn.pack(pady=3)
    return btn


mk_btn('Save Session',back.save_login)
mk_btn('Instagram.com',back.initialize_driver)
mk_btn('Download Bookmarks',back.download_latest)

root.mainloop()
