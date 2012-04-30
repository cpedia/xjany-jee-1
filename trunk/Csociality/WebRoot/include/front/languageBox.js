var langItemOver = false;
var heightLB;

function prepareLanguageBox() {
    $('.languageBox').css('visibility', 'hidden');
    $('.languageBox').show();
    
    //you need this to direct dropdowm up
    //heightLB = $('.languageBox').attr('clientHeight') + 12;
    //$('.languageBox').css('margin-top', -heightLB + 'px');
    
    var widthLB = $('.languageBox').attr('clientWidth');
    $('.languageBox a').css('width', widthLB + 'px');
    $('.languageBox').hide();
    $('.languageBox').css('visibility', 'visible');
}

$(document).ready(function () {
    prepareLanguageBox();

    $('body').click(function () {
        if (!(langItemOver)) {
            $('.languageBox').hide('slow');
        }
    });

    $('body').keydown(function (e) {
        if (e.keyCode == 27)
            $('.languageBox').hide('slow');
    });

    $('.languages > a')
        .click(function () {
            $('.languageBox').toggle('slow');
            return false;
        });

    $('.languageBox > a')
        .mouseover(function () { langItemOver = true; })
        .mouseout(function () { langItemOver = false; })
        .click(function () {        
            var langCode = $(this).attr('langCode');
            var hostName = $(this).attr('hostName');
            if (hostName != '' && hostName != null) {
            	hostName = hostName.substring(0, hostName.length - 1);//remove last /
            } else {
				hostName = '';
			}
            if (langCode != undefined && !($(this).hasClass('selectedLang'))) {
                $('.languageBox').hide('slow');

                if (location.href.indexOf('/admin') != -1)
                    window.location.href = hostName + "/admin/index_" + langCode + ".html";
                else
                    window.location.href = hostName + "/index_" + langCode + ".html";
            }
            return false;
        });
});