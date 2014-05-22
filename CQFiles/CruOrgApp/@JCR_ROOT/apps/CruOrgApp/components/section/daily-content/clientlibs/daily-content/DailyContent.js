/*Daily Content*/
var Cru = (function(v) {
    return v;
}(Cru || {}));  
Cru.components = (function(v) {
    return v;
}(Cru.components || {}));

Cru.components.DailyContent = (function(v) {
    return v;
}(Cru.components.DailyContent || {}));
Cru.components.DailyContent.elements = {
    H1 : "h1",
    H2 : "h2",
    UL : "ul",
    LI : "li",
    IMG : "img",
    A : "a",
    TIME : "time",
    SPAN : "span",
    FIGCAPTION : "figcaption",
    DIV : "div",
    createTitle: function(title){
        var h1 = document.createElement(this.H1);
        var titleNode = document.createTextNode(title);
        h1.appendChild(titleNode);
        return h1;
    },
    createSubtitle : function(subtitle){
        var h2 = document.createElement(this.H2);
        var subtitleNode = document.createTextNode(subtitle);
        h2.className = "post-subtitle  h3";
        h2.appendChild(subtitleNode);

        return h2;
    },
    createMetadata : function(date, twitterUser, author){
        var ul = document.createElement(this.UL);
        ul.className = "post-meta";
        if(author){
            var authorLi = document.createElement(this.LI);
            authorLi.className = "accent";
            var authorNode = document.createTextNode("by " + author);
            authorLi.appendChild(authorNode);
            ul.appendChild(authorLi);
        }

        if(twitterUser){
            var twitterUserLi = document.createElement(this.LI);
            twitterUserLi.className = "accent";
            var twitterUserAnchor = document.createElement((this.A));
            var twitterUserNode = document.createTextNode("@" + twitterUser);
            var twitterUserLink = "https://twitter.com/" + twitterUser;
            twitterUserAnchor.appendChild(twitterUserNode);
            twitterUserAnchor.href = twitterUserLink;
            twitterUserLi.appendChild(twitterUserAnchor);
            ul.appendChild(twitterUserLi);
        }

        if(date){
            var dateLi = document.createElement(this.LI);
            var dateTime = document.createElement(this.TIME);
            var dateNode = document.createTextNode(date);
            dateTime.appendChild(dateNode);
            dateLi.appendChild(dateTime);
            ul.appendChild(dateLi);
        }

        return ul;
    },
    createImage : function(src){
        var imageTag = document.createElement(this.IMG);
        imageTag.src = src;
        return imageTag;
    },
    createCaption : function(caption, credit){
        var captionTag = document.createElement(this.FIGCAPTION);
        captionTag.className = "image__caption default-view";
        var captionNode = document.createTextNode(caption);
        captionTag.appendChild(captionNode);
        if(credit){
            captionTag.appendChild(this.createCredit(credit));
        }
        return captionTag;
    },
    createCredit : function(credit){
        var creditTag = document.createElement(this.SPAN);
        creditTag.className = "image-credit";
        var creditNode = document.createTextNode(credit);
        creditTag.appendChild(creditNode);
        return(creditTag);
    },
    createPaginationItem : function(path, cssClass, text){
        var paginationTag = document.createElement(this.LI);
        paginationTag.className = cssClass;
        var paginationLink = document.createElement(this.A);
        paginationLink.className = "button  button--small  button--subtle";
        paginationLink.href = path + ".html";
        var paginationText = document.createTextNode(text);
        paginationLink.appendChild(paginationText);
        paginationTag.appendChild(paginationLink);
        return paginationTag;

    },
    createFacebookCommentsContainer : function(href, colorScheme, numberOfPosts){
        /*<div class="fb-comments" data-href="{%page.requestURL%}" data-width="700" data-numposts="{%#if global.numberOfPosts %}{% global.numberOfPosts %}{%else%}{%#if page.isEditMode %}3{%/if %}{%/if%}" data-colorscheme=""></div> */

        var container = document.createElement(this.DIV);
        container.className = "fb-comments";
        container.setAttribute("data-href", href);
        container.setAttribute("data-numposts", numberOfPosts);
        container.setAttribute("data-colorscheme", colorScheme);
        container.setAttribute("data-width", "700");
        return container;
    },
    buildHeader : function(header, data){
        var title = data.page["title"];
        if(title){
            header.append(this.createTitle(title));
        }

        var subtitle =  data.page["subtitle"];
        if(subtitle){
            header.append(this.createSubtitle(subtitle));
        }

        var date = data.page["dateText"];
        var twitterUser = data.page["twitterUser"];
        var author = data.page["author"];
        if(date || twitterUser || author){
            header.append(this.createMetadata(date, twitterUser, author));
        }

    },
    buildFigure : function(figure, data){
        var imagePath = data.page["imagePath"];
        if(imagePath){
            figure.append(this.createImage(imagePath));
        }

        var caption = data.page["imageCaption"];
        var credit = data.page["imageCredit"];
        if(caption){
            figure.append(this.createCaption(caption, credit));
        }
    },
    buildPagination : function(pagination, paths, data){
        var dailyContentPreviousLabel = "Previous";
        var dailyContentNextLabel = "Previous";
        if(data.global && data.global["dailyContentPreviousLabel"] != ""){
            dailyContentPreviousLabel = data.global["dailyContentPreviousLabel"];
        }
        if(data.global && data.global["dailyContentNextLabel"] != ""){
            dailyContentNextLabel = data.global["dailyContentNextLabel"];
        }
        if(paths.yesterday && !paths.isYesterdayDefaultPath){
            pagination.append(this.createPaginationItem(paths.yesterday, "pagination__prev  grid__item  one-half  text--left visibility--hide", dailyContentPreviousLabel));
        }
        if(paths.tomorrow && !paths.isTomorrowDefaultPath){
            pagination.append(this.createPaginationItem(paths.tomorrow, "pagination__next  text--right", dailyContentNextLabel));
        }
    },
    buildFacebookComments : function(container, data){
        if(data.page.requestURL){
            container.append(this.createFacebookCommentsContainer(data.page.requestURL, data.global.colorScheme, data.global.numberOfPosts));
        }
    },
    isEditMode : function(){
        if(typeof CQ != 'undefined'){
            if(CQ.WCM.isEditMode()){
               return true;
            }
        }
        return false;
    }
};