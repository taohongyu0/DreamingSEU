function addBold() {
    document.execCommand('bold', false, null);
    updatePreview();
}

function addItalic() {
    document.execCommand('italic', false, null);
    updatePreview();
}

function addUnderline() {
    document.execCommand('underline', false, null);
    updatePreview();
}

function updatePreview() {
    const editorContent = document.getElementById('editor').innerHTML;
    const previewArea = document.getElementById('preview');
    //previewArea.innerHTML = editorContent;
}

// Initialize the preview area with the initial content of the editor
document.addEventListener('DOMContentLoaded', function() {
    updatePreview();

    // Listen for changes in the editor to update the preview in real-time
    const editor = document.getElementById('editor');
    editor.addEventListener('input', updatePreview);
});