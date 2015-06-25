// Form to be sent
var form = document.getElementById('specUploads');

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
    upload.innerHTML = 'Uploading...';
    $('#loading').show();

    // Reset file checks
    fileMissing = false;
    wrongExtension = false;

    // Get files from the input
    var structuralFile = document.getElementById('structuralSpec').files[0];
    if(typeof structuralFile === 'undefined'){
        document.getElementById('ss').style.color = "#FF0000";
        fileMissing = true;
    } else {document.getElementById('ss').style.color = "#000000";}

    var functionalFile = document.getElementById('functionalSpec').files[0];
    if(typeof functionalFile === 'undefined'){
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

    // Check if extensions are correct
    if(structuralFile.name.substring(structuralFile.name.length - 3, structuralFile.name.length) !== 'eug') {
        document.getElementById('ss').style.color = "#FF0000";
        wrongExtension = true;
    } else {document.getElementById('ss').style.color = "#000000";}

    if(functionalFile.name.substring(functionalFile.name.length - 3, functionalFile.name.length) !== 'txt') {
        document.getElementById('fs').style.color = "#FF0000";
        wrongExtension = true;
    } else {document.getElementById('fs').style.color = "#000000";}

    // If wrong extensions are used, alert the user and start over
    if(wrongExtension){
        alert("Select files with the correct extensions (problematic fields shown in red)!");
        upload.innerHTML = 'Upload';
        $('#loading').hide();
        return;
    }

    // Create FormData object (if it is necessary to know the uploaded file's names, uncomment the names below)
    var formData = new FormData();
    formData.append('structuralSpec', structuralFile/*, plasmidFile.name*/);
    formData.append('functionalSpec', functionalFile/*, featureFile.name*/);

    // Let servlet know what to expect
    formData.append('mode','specification');

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
                window.location = "tree.html";
            } else {$('#loading').hide();}
        }
    });
};
