// Form to be sent
var form = document.getElementById('fileUploads');

// Upload button
var upload = document.getElementById('uploadButton');

// File checks
var fileMissing = false;
var wrongExtension = false;

// Called when upload/submit button is pressed
form.onsubmit = function(event) {
    // Prevent default form from sending
    event.preventDefault();

    // Update button text, show loading wheel
    upload.disabled = true;
    upload.innerHTML = 'Uploading...';
    $('#loading').show();

    // Reset file checks
    fileMissing = false;
    wrongExtension = false;

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

    var fluoresenceFile = document.getElementById('fSpectra').files[0];
    if(typeof fluoresenceFile === 'undefined'){
        document.getElementById('fs').style.color = "#FF0000";
        fileMissing = true;
    } else {document.getElementById('fs').style.color = "#000000";}

    // If files are missing, alert the user and start over
    if(fileMissing){
        alert("Select files for missing fields (shown in red)!");
        upload.innerHTML = 'Upload';
        $('#loading').hide();
        upload.disabled = false;
        return;
    }

    // Check if extensions are correct
    if(plasmidFile.name.substring(plasmidFile.name.length - 2, plasmidFile.name.length) !== 'gb') {
        document.getElementById('gpl').style.color = "#FF0000";
        wrongExtension = true;
    } else {document.getElementById('gpl').style.color = "#000000";}

    if(featureFile.name.substring(featureFile.name.length - 2, featureFile.name.length) !== 'gb') {
        document.getElementById('gfl').style.color = "#FF0000";
        wrongExtension = true;
    } else {document.getElementById('gfl').style.color = "#000000";}

    if(flowFile.name.substring(flowFile.name.length - 3, flowFile.name.length) !== 'csv') {
        document.getElementById('fcc').style.color = "#FF0000";
        wrongExtension = true;
    } else {document.getElementById('fcc').style.color = "#000000";}

    if(fluoresenceFile.name.substring(fluoresenceFile.name.length - 3, fluoresenceFile.name.length) !== 'csv') {
        document.getElementById('fs').style.color = "#FF0000";
        wrongExtension = true;
    } else {document.getElementById('fs').style.color = "#000000";}

    // If wrong extensions are used, alert the user and start over
    if(wrongExtension){
        alert("Select files with the correct extensions (problematic fields shown in red)!");
        upload.innerHTML = 'Upload';
        $('#loading').hide();
        upload.disabled = false;
        return;
    }

    // Create FormData object (if it is necessary to know the uploaded file's names, uncomment the names below)
    var formData = new FormData();
    formData.append('plasmidLib', plasmidFile/*, plasmidFile.name*/);
    formData.append('featureLib', featureFile/*, featureFile.name*/);
    formData.append('fcConfig', flowFile/*, flowFile.name*/);
    formData.append('fSpectra', fluoresenceFile/*, fluoresenceFile.name*/);

    formData.append('plasmidLibName', plasmidFile.name);
    formData.append('featureLibName', featureFile.name);
    formData.append('fcConfigName', flowFile.name);
    formData.append('fSpectraName', fluoresenceFile.name);

    // Let servlet know what to expect
    formData.append('mode','upload');

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
                window.location = "specification.html";
            } else {$('#loading').hide();}
        }
    });
};
