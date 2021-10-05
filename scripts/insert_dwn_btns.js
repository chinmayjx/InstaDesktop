async function fn() {
    btn_size = 35;
    styl = `
    .dwn_btn{
      color: #dedede;
      position:absolute;
      bottom:-0.1cm;
      right:-0.1cm;
      width:${btn_size}px;
      height:${btn_size}px;
      background: #000000;
      border:None;
      outline:None;
      border-radius: 20px 0px 0px 0px;
      opacity: 1
    }
    .dwn_btn:hover{
      opacity: 0.6;
    }
  `;
    stylel = document.createElement('style');
    stylel.innerHTML = styl;
    document.getElementsByTagName("head")[0].appendChild(stylel);


    btn_html = `
    <img
      class='dwn_btn'
      src='https://instagram.fbho4-1.fna.fbcdn.net/v/t51.2885-19/s150x150/243371348_577262953591263_6054374812329466402_n.jpg?_nc_ht=instagram.fbho4-1.fna.fbcdn.net&_nc_ohc=k1ni248a-i8AX-kEMNd&edm=AGeOuZUBAAAA&ccb=7-4&oh=2c20c46c83506b7034c820aedcad0b5c&oe=615E2C1F&_nc_sid=924bfa'
    >
  `;

    function insert_btns() {
        prnt = document.querySelectorAll("._bz0w");
        e = prnt[9];
        for (var i = 0; i < prnt.length; i++) {
            atag = prnt[i].querySelector("a");
            if (!atag) {
                continue;
            }
            if (!prnt[i].querySelector(".dwn_btn")) {
                var btn = document.createElement("div");
                btn.innerHTML = btn_html;
                var tmp = atag.getAttribute('href');
                btn.setAttribute("onclick", `console.log('flag_hwv:download_post');console.log('visit:${tmp}');`);
                prnt[i].appendChild(btn);
            }
        }
    }

    await new Promise(r => {
        let inter = setInterval(() => {
            if (document.querySelector("._bz0w a")) {
                console.log("insert btns");
                insert_btns();
                r();
                clearInterval(inter);
            }
        }, 100);
    });
    window.onscroll = insert_btns;
}

fn();