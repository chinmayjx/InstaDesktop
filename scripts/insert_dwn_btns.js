async function fn() {
    btn_size = 35;
    styl = `
    .container{
      width:35px;
          height:35px;
          color: #dedede;
          position:absolute;
          bottom:-0.1cm;
          right:-0.1cm;
          padding:0.1rem;
          box-sizing: border-box;
          border-radius: 20px 0px 0px 0px;
          background: white;
    }

    .dwn_btn{
          position:absolute;
          top:50%;
          left:50%;
          transform: translate(-50%,-50%);
          width:50%;
          height:50%;
          border:None;
          outline:None;

          opacity: 1
        }
    .container:hover{
      opacity: 0.6;
    }
  `;
    stylel = document.createElement('style');
    stylel.innerHTML = styl;
    document.getElementsByTagName("head")[0].appendChild(stylel);


//    btn_html = `
//    <img
//      class='dwn_btn'
//      src='https://instagram.fbom17-1.fna.fbcdn.net/v/t51.2885-19/s150x150/243371348_577262953591263_6054374812329466402_n.jpg?_nc_ht=instagram.fbom17-1.fna.fbcdn.net&_nc_cat=103&_nc_ohc=cT1GcNWJiv8AX870AZI&edm=ABfd0MgBAAAA&ccb=7-4&oh=00_AT-56tL8t8Pvfr-hL-kiw9CN82mls3Tw91g4Thui6t3Qnw&oe=61F4771F&_nc_sid=7bff83'
//    >
//  `;
    btn_html=`<div class='container'>
              <svg class='dwn_btn' version="1.1" id="Capa_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
              	 viewBox="0 0 489.701 489.701" style="enable-background:new 0 0 489.701 489.701;" xml:space="preserve">
              <g>
              	<g>
              		<g>
              			<path d="M244.9,0c-9.5,0-17.1,7.7-17.1,17.2v312.3l-77.6-77.6c-6.7-6.7-17.6-6.7-24.3,0c-6.7,6.7-6.7,17.6,0,24.3l106.9,106.9
              				c3.2,3.2,7.6,5,12.1,5c4.6,0,8.9-1.8,12.1-5l106.9-107c6.7-6.7,6.7-17.6,0-24.3s-17.6-6.7-24.3,0L262,329.4V17.2
              				C262.1,7.7,254.4,0,244.9,0z"/>
              			<path d="M455.8,472.6c0-9.5-7.7-17.2-17.2-17.2H51.1c-9.5,0-17.2,7.7-17.2,17.2s7.7,17.1,17.2,17.1h387.6
              				C448.201,489.8,455.8,482.1,455.8,472.6z"/>
              		</g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              	<g>
              	</g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              <g>
              </g>
              </svg>
              </div>
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