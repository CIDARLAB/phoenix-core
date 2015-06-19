// Form to be sent
var form = document.getElementById('fileUploads');

// Upload button
var upload = document.getElementById('uploadButton');

// Check if all files are present
var fileMissing = false;

// Called when upload/submit button is pressed
form.onsubmit = function(event) {
    // Prevent default form from sending
    event.preventDefault();

    // Update button text, show loading wheel
    upload.innerHTML = 'Uploading...';
    $('#loading').show();

    // Reset file checker
    fileMissing = false;

    // Get files from the input
    var plasmidFile = document.getElementById('plasmidLib').files[0];
    if(typeof plasmidFile === 'undefined'){
        document.getElementById('gpl').style.color = "#FF0000";
        fileMissing = true;
    } else {document.getElementById('gpl').style.color = "#000000";}

    var featureFile = document.getElementById('featureLib').files[0];
    if(typeof featureFile === 'undefined'){
        document.getElementById('gfl').style.color = "#FF0000";
        fileMissing = true;
    } else {document.getElementById('gfl').style.color = "#000000";}

    var flowFile = document.getElementById('fcConfig').files[0];
    if(typeof flowFile === 'undefined'){
        document.getElementById('fcc').style.color = "#FF0000";
        fileMissing = true;
    } else {document.getElementById('fcc').style.color = "#000000";}

    var flouresenceFile = document.getElementById('fSpectra').files[0];
    if(typeof flouresenceFile === 'undefined'){
        document.getElementById('fs').style.color = "#FF0000";
        fileMissing = true;
    } else {document.getElementById('fs').style.color = "#000000";}

    // If files are missing, alert the user and start over
    if(fileMissing){
        alert("Select files for missing fields (shown in red)!");
        upload.innerHTML = 'Upload';
        $('#loading').hide();
        return;
    }

    // Further file checking done here if desired

    // Create FormData object (if it is necessary to know the uploaded file's names, uncomment the names below)
    var formData = new FormData();
    formData.append('plasmidLib', plasmidFile/*, plasmidFile.name*/);
    formData.append('featureLib', featureFile/*, featureFile.name*/);
    formData.append('fcConfig', flowFile/*, flowFile.name*/);
    formData.append('fSpectra', flouresenceFile/*, flouresenceFile.name*/);

    // Send ajax form
    $.ajax({
        url: 'ClientServlet',
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        type: 'POST',
        success: function(data){
            // Disable upload/submit button and redirect on success
            upload.disabled = true;
            upload.innerHTML = data;
            if(upload.innerHTML === "Done!"){
                $('#loading').hide();
                window.location = "dynamicForm2.html";
            } else {$('#loading').hide();}
        }
    });
};
