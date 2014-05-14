function emailThis(){
    window.location = "mailto:?subject="+escape("Article from Cru.org: " + document.title)+"&body="+document.title+"%0A"+escape(location.href);
}