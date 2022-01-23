async function image_links(){
  let cur_url = window.location.href.split("com").at(-1);
  let TL = 5000;
  let lt = 0;
  let set = new Set();
  function getBestImage(srcset){
    let srcs=srcset.split(',');
    let l,mx=0;
    for(let i=0;i<srcs.length;i++){
      srcs[i]=srcs[i].trim().split(' ');
      srcs[i][1]=parseInt(srcs[i][1].trim().substring(0,srcs[i][1].trim().length-1));
      if(srcs[i][1]>mx){
        l=srcs[i][0];
        mx=srcs[i][1];
      }
    }
    console.log('from',srcs, 'found best image of width ', mx)
    return l;
  }
  function update_set() {
    for (let i = 0; i < img.length; i++) {
      let srcset = img[i].getAttribute('srcset');
      if(!srcset) continue;
      let tmp = getBestImage(srcset);
      if (tmp){
        if(!set.has(tmp)){
          console.log(set.size);
          console.log("download:"+cur_url+" "+tmp);
          set.add(tmp);
        }
      }
    }
  }

  let img = document.querySelectorAll(".ZyFrc .FFVAD");
  console.log(img)
  lt = new Date().getTime();
  while(img.length == 0 && new Date().getTime() - lt < TL){
    await new Promise(r => setTimeout(r,100));
    img = document.querySelectorAll(".ZyFrc .FFVAD");
    console.log("waiting for page load");
  }
  
  let n_posts = document.querySelectorAll(".Yi5aA").length;
  if(img.length > 0 && n_posts==0) n_posts=1;
  console.log("download_cnt:"+n_posts);
  
  if(img.length == 1){
    console.log("only one image");
    update_set();
  }
  else{
    let rb = document.querySelector(".coreSpriteRightChevron");
    lt = new Date().getTime();
    while(!rb && new Date().getTime() - lt < TL){
      await new Promise(r => setTimeout(r,100));
      rb = document.querySelector(".coreSpriteRightChevron");
      console.log("waiting for right button")
    }
    update_set();
    while(rb){
      rb.click();
      await new Promise(r => setTimeout(r,100));
      img = document.querySelectorAll(".ZyFrc .FFVAD");
      update_set();
      rb = document.querySelector(".coreSpriteRightChevron");
    }
  }
  console.log(`found ${set.size} images`);
}

image_links();















