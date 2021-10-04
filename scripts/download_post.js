async function wait_for_element(sel) {
  var el = document.querySelector(sel);
  var ct = 0;
  while (!el && ct < 100) {
    await new Promise(r => setTimeout(r, 100));
    el = document.querySelector(sel);
    console.log('waiting')
    ct++;
  }
  return el
}
async function get_links() {
  var set = new Set();
  function update_set() {
    var img = document.querySelectorAll(".ZyFrc  .KL4Bh > img");
    for (var i = 0; i < img.length; i++) {
      var srcset = img[i].getAttribute('srcset');
      if(!srcset) continue;
      var tmp = srcset.split(',').at(-1).split(' ')[0];
      if (tmp) set.add(tmp);
    }
  }
  if (!document.querySelector(".Yi5aA")) {
    console.log('only');
    update_set();
  }
  else {
    var rb = await wait_for_element(".coreSpriteRightChevron");
    while (rb) {
      rb = document.querySelector(".coreSpriteRightChevron");
      update_set();
      await new Promise(r => setTimeout(r, 10));
      if (rb) rb.click()
    }
  }
  set.forEach(function (e) {
    console.log("download:" + e);
  });
}

get_links();
