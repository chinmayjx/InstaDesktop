async function wait_for_element(sel){
  var el = document.querySelector(sel);
  while(!el){
  	await new Promise(r => setTimeout(r,100));
    el = document.querySelector(sel);
    console.log('waiting')
  }
  return el
}
async function get_links(){
	var set = new Set();
  var rb = await wait_for_element(".coreSpriteRightChevron");
  while(rb){
    rb = document.querySelector(".coreSpriteRightChevron");
    var img = document.querySelectorAll(".ZyFrc  .KL4Bh > img");
    for(var i=0;i<img.length;i++){
      var tmp = img[i].getAttribute('src'); 
      if(tmp) set.add(tmp);
    }
    console.log(img);
    await new Promise(r => setTimeout(r,10));
    if(rb) rb.click()
  }
  set.forEach(function(e){
    console.log("download:"+e);
  });
}

get_links();
	
