async function fn(){
  async function get_all_links(){
    return new Promise(resolve => {
      var inf_scroll = setInterval(function (){
        get_links()
        window.scrollTo(0,document.body.scrollHeight);
        setTimeout(function(){
          if(document.querySelector(".ZUqME")){
            console.log("loading more");
          }
          else{
            clearInterval(inf_scroll);
            resolve();
          }
        },100);
      },1000);  
    });
  }

  var links = new Set()
  function get_links(){
    var l = document.querySelectorAll("._bz0w a")
    l.forEach(function(a){
      links.add(a.getAttribute('href'))
    });
  }
  await get_all_links();
  links = Array.from(links);
  console.log(links);
  return links;
}

fn();