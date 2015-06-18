function upload(){
    var formData = new FormData($('#fileUploads')[0]);
    console.log(formData);
    $.ajax({
        url: 'ClientServlet',
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        type: 'POST',
        success: function(data){
            alert(data);
        }
    });
}

// $('#displayImage').change(function(){
        // 
//                 var formData = new FormData($('#pigeonImage')[0]);
//                 console.log(formData);
//                 $.ajax({
//                             url: 'https://owlimageserver.herokuapp.com/pigeon',  //Server script to process data
//                             type: 'POST',
//                             // Form data
//                             data: formData,
//                             //Options to tell jQuery not to process data or worry about content-type.
//                             cache: false,
//                             contentType: false,
//                             processData: false
//                         });  