async function run(){
  await new Promise(r => {
    setInterval(()=>{
      if(document.querySelector("._bz0w")) r();
    },500);
  });
  if(window.scrollY>20){
    console.log('go up')
    window.scrollTo(0, 0);
  	await new Promise(r => {setTimeout(r,1000);});
  }
  async function scr(){
    var lp = window.scrollY;
    var lt = new Date().getTime();
    var loading = true;
    while(loading){
      await get_links();
      console.log(links.size);
      lp = window.scrollY;
      lt = new Date().getTime();
      while(window.scrollY == lp){
        if(!document.querySelector(".ZUqME") && new Date().getTime() - lt > 1000){
          loading = false;
          console.log('loaded');
          break;
        }

        window.scrollTo(0,window.scrollY+window.innerHeight);
        await new Promise(r => {setTimeout(r,50);});
      }
    }
  }

  var links = new Set()
  async function get_links() {
    var l = document.querySelectorAll("._bz0w a")
    l.forEach(function (a) {
      links.add(a.getAttribute('href'))
    });
  }
  await scr();
  links = Array.from(links);
  console.log(links.length);
  links.forEach(ln =>{
    console.log("post_link:"+ln);
  });
  return links;
}

run();























