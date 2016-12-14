
function openCreateOptionInputDialog(){
	$jquery("#addOption").dialog({
		autoOpen: true,
		height: 280,
	    width: 600,
	    modal: true,
	    show: "clip",
	    hide: "clip",
	    dialogClass: "noTitleStuff"
	    

	});
	document.getElementById('addOption').style.visibility = "visible";
	$jquery("#addOption").css("height", "");

}

function getImage(val) {
    return '<img class="editImage" id=' + val + '  src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/edit.gif" style="width:20px;height:18px;cursor:pointer;" title="Edit" />' ;
}
function reloadOptionData() {
	optionDetailsTable.fnReloadAjax();
}
function addSafeDataValidation() {
	
	$jquery( "#addOptionForm").validate({
	  debug : true,
      rules: {
	      "addOptionForm:description": { 
	    	  required: true
	      }
	      
      },
      highlight: function(label) {
	    //$jquery(label).closest('.control-group').addClass('error');
	  },
	  success: function(label) {
		  label
	      .addClass('validImg')
	      .closest('.control-group').addClass('success');
	  }
  });
}
function updateOptionJS() {
	if ($jquery("#addOptionForm").valid()) {
		updateOptionAction();
	}
	return $jquery( "#addOptionForm").valid();
}
