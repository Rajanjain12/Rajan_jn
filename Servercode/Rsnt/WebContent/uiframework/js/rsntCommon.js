/**
 * 
 */

RSNT_GLOBALCONTEXTPATH = '/Rsnt';
RSNT_RESTEASYPATH = '/seam/resource/restv1';

function closeDialog(id) {
	$jquery(id).dialog('close');
	}

function ajaxPostCall(url, data, callBackFn) {
	$jquery.ajax({
	    url:url,
	    data: data,
	    dataType: "json",
	    type:"POST",
	    success: callBackFn
	});	
}

function ajaxGetCall(url, data, callBackFn) {
	$jquery.ajax({
	    url:url,
	    data: data,
	    dataType: "json",
	    type:"GET",
	    success: callBackFn
	});	
}

function openDialogBox(divId, title, callBkFn, loadingImageDiv) {
	$jquery(divId).dialog({
	  	title: title,
        show: "blind",
        hide: "explode",
  		width:'auto', height:'auto',
  		modal: true,
  		open: function (event, ui) {
  			
        },
        buttons: {
            Ok: function() {
            	if(loadingImageDiv) $jquery(loadingImageDiv).show();
            	if (callBkFn) {
            		callBkFn($jquery( this ).dialog( "close" ));
            	} else {
            		$jquery( this ).dialog( "close" );
            	}
            	
            },
            Cancel: function() {
                $jquery( this ).dialog( "close" );
            }
        }
    });
}

function openDialog(divId, title, loadingImageDiv, callBkFn) {
	$jquery(divId).dialog({
	  	title: title,
        show: "blind",
        hide: "explode",
  		width:'auto', height:'auto',
  		modal: true,
  		close: function(event,ui) {
  			if (callBkFn) callBkFn();
  		}
    });
}

function destroyDialogBox(divId) {
	$jquery( divId ).dialog( "destroy" );
}

function getEditImage(val) {
    return '<img class="editImage" id=' + val + ' src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/edit.gif" style="width:15px;height:12px;cursor:pointer;" title="Rename" />';
    }
function getLayoutMarkerImage(val, row) {
	if(row.activeStatus=='Y' && row.access=='1')
    return '<img class="editImage" id=' + val + ' src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/edit.gif" style="width:20px;height:18px;cursor:pointer;" title="Rename" />'
    + '<img class="deleteImage" id=' + val + ' src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/delete.gif" style="width:20px;height:18px;cursor:pointer;" title="Toggle Active Status" />'
    + '<img class="printImage" id=' + val + ' src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/print.jpeg" style="width:15px;height:12px;cursor:pointer;" title="Print QRCode" />';
	else if(row.access=='1')
		 return '<img class="editImage" id=' + val + ' src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/edit.gif" style="width:20px;height:18px;cursor:pointer;" title="Edit" />'
		    + '<img class="deleteImage" id=' + val + ' src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/delete.gif" style="width:20px;height:18px;cursor:pointer;" title="Toggle Active Status" />';
	else
		return '<img class="editImage" id=' + val + ' src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/edit.gif" style="width:20px;height:18px;cursor:pointer;" title="Edit" />';
	    
}

function getTargetDivAndShowMsg(errorDivId){
	
	var targetDiv='';
	var targetType='';
	if($jquery(errorDivId+' dt').attr('class')=='rich-messages-label-sucess')
		{
			targetDiv = '#customSucMsg';
			targetType = 'Success Message';
		}
		
	else
		{
			targetDiv='#customErrMsg';
			targetType = 'Error';
		}
		
	
	showRichErrMsg(errorDivId,targetDiv,targetType);
	
}
function showRichErrMsg(errorDivId, targetDiv, type){
	
	var pTargetDivVar = '#customErrMsg';
	var pTargetType = 'Error';
	
	if(targetDiv){
		pTargetDivVar = targetDiv;
	}
	if(type){
		pTargetType = type;
	}
	
	if($jquery(errorDivId).find("span").text()!=""){
		
		$jquery(pTargetDivVar).find(".inner").html("");
		$jquery(pTargetDivVar).show();
		advalidMsg = '<p><span>'+pTargetType+'</span><br>'+$jquery(errorDivId).find("span").text()+'</p>';
		$jquery(pTargetDivVar).find(".inner").html(advalidMsg);
		document.getElementById('topLink').click();
	}

 }

function hideErrMsgDivs(){
	
	$jquery('.error-msg').hide();
}

function updateActiveMenuItem(itmVar){
	
	$jquery(itmVar).addClass('active');
	
}

function getImage(val, row) {
	if(row.access=="1"){
		 return '<img class="deleteImage" id=' + val + ' src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/delete.gif" style="width:20px;height:18px;cursor:pointer;" title="Toggle Active Status" />';
	}
	else{
		return "";
	}
   
}