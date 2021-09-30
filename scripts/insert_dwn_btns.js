styl = `
  #xy{
  	color: #dedede;
    text-shadow: 3px 1px #383838;
    position:absolute;
    bottom:0.25cm;
    right:0.5cm;
    width:1.5cm;
    height:1.5cm;
    font-size:2cm;
    text-aligh: center;
    background-color: transparent;
    border:None
  }
  #xy:hover{
  	color: #383838;
  }
`
stylel = document.createElement('style')
stylel.innerHTML = styl
document.getElementsByTagName("head")[0].appendChild(stylel);

function btncl(ln){
  alert(ln)
}
prnt = document.querySelectorAll("._bz0w")
prnt.forEach(function(e){
	btn = document.createElement("p")
  btn.innerHTML = "<button id='xy' onclick=btncl(this.id)>▼</button>"
  e.appendChild(btn)
})
