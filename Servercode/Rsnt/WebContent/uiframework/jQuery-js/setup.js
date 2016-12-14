function setSidebarHeight(){
	setTimeout(function(){
var height = $jquery(document).height();
    $jquery('.grid_12').each(function () {
        height -= $jquery(this).outerHeight();
    });
    height -= $jquery('#site_info').outerHeight();
	height-=1;
	//salert(height);
    $jquery('.sidemenu').css('height', height);					   
						},100);
}

//Dashboard chart
function setupDashboardChart(containerElementId) {
    var s1 = [200, 300, 400, 500, 600, 700, 800, 900, 1000];
    var s2 = [190, 290, 390, 490, 590, 690, 790, 890, 990];
    var s3 = [250, 350, 450, 550, 650, 750, 850, 950, 1050];
    var s4 = [2000, 1600, 1400, 1100, 900, 800, 1550, 1950, 1050];
    // Can specify a custom tick Array.
    // Ticks should match up one for each y value (category) in the series.
    var ticks = ['March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November'];

    var plot1 = $jquery.jqplot(containerElementId, [s1, s2, s3, s4], {
        // The "seriesDefaults" option is an options object that will
        // be applied to all series in the chart.
        seriesDefaults: {
            renderer: $jquery.jqplot.BarRenderer,
            rendererOptions: { fillToZero: true }
        },
        // Custom labels for the series are specified with the "label"
        // option on the series option.  Here a series option object
        // is specified for each series.
        series: [
            { label: 'Apples' },
            { label: 'Oranges' },
            { label: 'Mangoes' },
            { label: 'Grapes' }
        ],
        // Show the legend and put it outside the grid, but inside the
        // plot container, shrinking the grid to accomodate the legend.
        // A value of "outside" would not shrink the grid and allow
        // the legend to overflow the container.
        legend: {
            show: true,
            placement: 'outsideGrid'
        },
        axes: {
            // Use a category axis on the x axis and use our custom ticks.
            xaxis: {
                renderer: $jquery.jqplot.CategoryAxisRenderer,
                ticks: ticks
            },
            // Pad the y axis just a little so bars can get close to, but
            // not touch, the grid boundaries.  1.2 is the default padding.
            yaxis: {
                pad: 1.05,
                tickOptions: { formatString: '$%d' }
            }
        }
    });
}
//points charts

function drawPointsChart(containerElement) {


    var cosPoints = [];
    for (var i = 0; i < 2 * Math.PI; i += 0.4) {
        cosPoints.push([i, Math.cos(i)]);
    }

    var sinPoints = [];
    for (var i = 0; i < 2 * Math.PI; i += 0.4) {
        sinPoints.push([i, 2 * Math.sin(i - .8)]);
    }

    var powPoints1 = [];
    for (var i = 0; i < 2 * Math.PI; i += 0.4) {
        powPoints1.push([i, 2.5 + Math.pow(i / 4, 2)]);
    }

    var powPoints2 = [];
    for (var i = 0; i < 2 * Math.PI; i += 0.4) {
        powPoints2.push([i, -2.5 - Math.pow(i / 4, 2)]);
    }

    var plot3 = $jquery.jqplot(containerElement, [cosPoints, sinPoints, powPoints1, powPoints2],
    {
        title: 'Line Style Options',
        // Series options are specified as an array of objects, one object
        // for each series.
        series: [
          {
              // Change our line width and use a diamond shaped marker.
              lineWidth: 2,
              markerOptions: { style: 'dimaond' }
          },
          {
              // Don't show a line, just show markers.
              // Make the markers 7 pixels with an 'x' style
              showLine: false,
              markerOptions: { size: 7, style: "x" }
          },
          {
              // Use (open) circlular markers.
              markerOptions: { style: "circle" }
          },
          {
              // Use a thicker, 5 pixel line and 10 pixel
              // filled square markers.
              lineWidth: 5,
              markerOptions: { style: "filledSquare", size: 10 }
          }
      ]
    }
  );

}

//draw pie chart
function drawPieChart(containerElement) {
    var data = [
    ['Heavy Industry', 12], ['Retail', 9], ['Light Industry', 14],
    ['Out of home', 16], ['Commuting', 7], ['Orientation', 9]
  ];
    var plot1 = jQuery.jqplot('chart1', [data],
    {
        seriesDefaults: {
            // Make this a pie chart.
            renderer: jQuery.jqplot.PieRenderer,
            rendererOptions: {
                // Put data labels on the pie slices.
                // By default, labels show the percentage of the slice.
                showDataLabels: true
            }
        },
        legend: { show: true, location: 'e' }
    }
  );
}
//draw donut chart
function drawDonutChart(containerElement) {
    var s1 = [['a', 6], ['b', 8], ['c', 14], ['d', 20]];
    var s2 = [['a', 8], ['b', 12], ['c', 6], ['d', 9]];

    var plot3 = $jquery.jqplot(containerElement, [s1, s2], {
        seriesDefaults: {
            // make this a donut chart.
            renderer: $jquery.jqplot.DonutRenderer,
            rendererOptions: {
                // Donut's can be cut into slices like pies.
                sliceMargin: 3,
                // Pies and donuts can start at any arbitrary angle.
                startAngle: -90,
                showDataLabels: true,
                // By default, data labels show the percentage of the donut/pie.
                // You can show the data 'value' or data 'label' instead.
                dataLabels: 'value'
            }
        }
    });
}

//draw bar chart
function drawBarchart(containerElement) {
    var s1 = [200, 600, 700, 1000];
    var s2 = [460, -210, 690, 820];
    var s3 = [-260, -440, 320, 200];
    // Can specify a custom tick Array.
    // Ticks should match up one for each y value (category) in the series.
    var ticks = ['May', 'June', 'July', 'August'];

    var plot1 = $jquery.jqplot(containerElement, [s1, s2, s3], {
        // The "seriesDefaults" option is an options object that will
        // be applied to all series in the chart.
        seriesDefaults: {
            renderer: $jquery.jqplot.BarRenderer,
            rendererOptions: { fillToZero: true }
        },
        // Custom labels for the series are specified with the "label"
        // option on the series option.  Here a series option object
        // is specified for each series.
        series: [
            { label: 'Hotel' },
            { label: 'Event Regristration' },
            { label: 'Airfare' }
        ],
        // Show the legend and put it outside the grid, but inside the
        // plot container, shrinking the grid to accomodate the legend.
        // A value of "outside" would not shrink the grid and allow
        // the legend to overflow the container.
        legend: {
            show: true,
            placement: 'outsideGrid'
        },
        axes: {
            // Use a category axis on the x axis and use our custom ticks.
            xaxis: {
                renderer: $jquery.jqplot.CategoryAxisRenderer,
                ticks: ticks
            },
            // Pad the y axis just a little so bars can get close to, but
            // not touch, the grid boundaries.  1.2 is the default padding.
            yaxis: {
                pad: 1.05,
                tickOptions: { formatString: '$%d' }
            }
        }
    });
}
//draw bubble chart
function drawBubbleChart(containerElement) {

    var arr = [[11, 123, 1236, ""], [45, 92, 1067, ""],
  [24, 104, 1176, ""], [50, 23, 610, "A"],
  [18, 17, 539, ""], [7, 89, 864, ""], [2, 13, 1026, ""]];

    var plot1b = $jquery.jqplot(containerElement, [arr], {
        seriesDefaults: {
            renderer: $jquery.jqplot.BubbleRenderer,
            rendererOptions: {
                bubbleAlpha: 0.6,
                highlightAlpha: 0.8,
                showLabels: false
            },
            shadow: true,
            shadowAlpha: 0.05
        }
    });

    // Legend is a simple table in the html.
    // Dynamically populate it with the labels from each data value.
    $jquery.each(arr, function (index, val) {
        $jquery('#' + containerElement).append('<tr><td>' + val[3] + '</td><td>' + val[2] + '</td></tr>');
    });

    // Now bind function to the highlight event to show the tooltip
    // and highlight the row in the legend. 
    $jquery('#' + containerElement).bind('jqplotDataHighlight',
    function (ev, seriesIndex, pointIndex, data, radius) {
        var chart_left = $jquery('#' + containerElement).offset().left,
        chart_top = $jquery('#' + containerElement).offset().top,
        x = plot1b.axes.xaxis.u2p(data[0]),  // convert x axis unita to pixels
        y = plot1b.axes.yaxis.u2p(data[1]);  // convert y axis units to pixels
        var color = 'rgb(50%,50%,100%)';
        $jquery('#tooltip1b').css({ left: chart_left + x + radius + 5, top: chart_top + y });
        $jquery('#tooltip1b').html('<span style="font-size:14px;font-weight:bold;color:' +
      color + ';">' + data[3] + '</span><br />' + 'x: ' + data[0] +
      '<br />' + 'y: ' + data[1] + '<br />' + 'r: ' + data[2]);
        $jquery('#tooltip1b').show();
        $jquery('#legend1b tr').css('background-color', '#ffffff');
        $jquery('#legend1b tr').eq(pointIndex + 1).css('background-color', color);
    });

    // Bind a function to the unhighlight event to clean up after highlighting.
    $jquery('#' + containerElement).bind('jqplotDataUnhighlight',
      function (ev, seriesIndex, pointIndex, data) {
          $jquery('#tooltip1b').empty();
          $jquery('#tooltip1b').hide();
          $jquery('#' + containerElement + ' tr').css('background-color', '#ffffff');
      });
}

//-------------------------------------------------------------- */
// Gallery Actions
//-------------------------------------------------------------- */

function initializeGallery() {
    // When hovering over gallery li element
    $jquery("ul.gallery li").hover(function () {

        var $image = (this);

        // Shows actions when hovering
        $jquery(this).find(".actions").show();

        // If the "x" icon is pressed, show confirmation (#dialog-confirm)
        $jquery(this).find(".actions .delete").click(function () {

            // Confirmation
            $jquery("#dialog-confirm").dialog({
                resizable: false,
                modal: true,
                minHeight: 0,
                draggable: false,
                buttons: {
                    "Delete": function () {
                        $jquery(this).dialog("close");

                        // Removes image if delete is pressed
                        $jquery($image).fadeOut('slow', function () {
                            $jquery($image).remove();
                        });

                    },

                    // Removes dialog if cancel is pressed
                    Cancel: function () {
                        $jquery(this).dialog("close");
                    }
                }
            });

            return false;
        });


        // Changes opacity of the image
        $jquery(this).find("img").css("opacity", "0.5");

        // On hover off
        $jquery(this).hover(function () {
        }, function () {

            // Hides actions when hovering off
            $jquery(this).find(".actions").hide();

            // Changes opacity of the image back to normal
            $jquery(this).find("img").css("opacity", "1");

        });
    });
}
function setupGallery() {

    initializeGallery();
    //-------------------------------------------------------------- */
    //
    // 	**** Gallery Sorting (Quicksand) **** 
    //
    // 	For more information go to:
    //	http://razorjack.net/quicksand/
    //
    //-------------------------------------------------------------- */

    $jquery('ul.gallery').each(function () {

        // get the action filter option item on page load
        var $fileringType = $jquery("ul.sorting li.active a[data-type]").first().before(this);
        var $filterType = $jquery($fileringType).attr('data-id');

        var $gallerySorting = $jquery(this).parent().prev().children('ul.sorting');

        // get and assign the ourHolder element to the
        // $jqueryholder varible for use later
        var $holder = $jquery(this);

        // clone all items within the pre-assigned $holder element
        var $data = $holder.clone();

        // attempt to call Quicksand when a filter option
        // item is clicked
        $jquery($gallerySorting).find("a[data-type]").click(function (e) {
            // reset the active class on all the buttons
            $jquery($gallerySorting).find("a[data-type].active").removeClass('active');

            // assign the class of the clicked filter option
            // element to our $filterType variable
            var $filterType = $jquery(this).attr('data-type');
            $jquery(this).addClass('active');
            if ($filterType == 'all') {
                // assign all li items to the $filteredData var when
                // the 'All' filter option is clicked
                var $filteredData = $data.find('li');
            }
            else {
                // find all li elements that have our required $filterType
                // values for the data-type element
                var $filteredData = $data.find('li[data-type=' + $filterType + ']');
            }

            // call quicksand and assign transition parameters
            $holder.quicksand($filteredData, {
                duration: 800,
                easing: 'easeInOutQuad',
                useScaling: true,
                adjustHeight: 'auto'
            }, function () {
                $jquery('.popup').facebox();
                initializeGallery();
            });

            return false;
        });

    });
}

//setup pretty-photo
function setupPrettyPhoto() {

    $jquery("a[rel^='prettyPhoto']").prettyPhoto();
}

//setup tinyMCE

function setupTinyMCE() {
    $jquery('textarea.tinymce').tinymce({
        // Location of TinyMCE script
        script_url: 'js/tiny-mce/tiny_mce.js',

        // General options
        theme: "advanced",
        plugins: "autolink,lists,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,advlist",

        // Theme options
        theme_advanced_buttons1: "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect",
        theme_advanced_buttons2: "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
        theme_advanced_buttons3: "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
        theme_advanced_buttons4: "insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak",
        theme_advanced_toolbar_location: "top",
        theme_advanced_toolbar_align: "left",
        theme_advanced_statusbar_location: "bottom",
        theme_advanced_resizing: true,

        // Example content CSS (should be your site CSS)
        content_css: "css/content.css",

        // Drop lists for link/image/media/template dialogs
        template_external_list_url: "lists/template_list.js",
        external_link_list_url: "lists/link_list.js",
        external_image_list_url: "lists/image_list.js",
        media_external_list_url: "lists/media_list.js",

        // Replace values for the template plugin
        template_replace_values: {
            username: "Some User",
            staffid: "991234"
        }
    });
}

//setup DatePicker
function setDatePicker(containerElement) {
    var datePicker = $jquery('#' + containerElement);
    datePicker.datepicker({
        showOn: "button",
        buttonImage: "img/calendar.gif",
        buttonImageOnly: true
    });
}
//setup progressbar
function setupProgressbar(containerElement) {
    $jquery("#" + containerElement).progressbar({
        value: 59
    });
}

//setup dialog box

function setupDialogBox(containerElement, associatedButton) {
    $jquery.fx.speeds._default = 1000;
    $jquery("#" + containerElement).dialog({
        autoOpen: false,
        show: "blind",
        hide: "explode"
    });

    $jquery("#" + associatedButton).click(function () {
        $jquery("#" + containerElement).dialog("open");
        return false;
    });
}

//setup accordion

function setupAccordion(containerElement) {
    $jquery("#" + containerElement).accordion();
}

//setup radios and checkboxes
//function setupGrumbleToolTip(elementid) {
//    initializeGrumble(elementid);
//    $('#' + elementid).focus(function () {
//        initializeGrumble(elementid);
//    });
//}

//function initializeGrumble(elementid) {
//    $('#' + elementid).grumble(
//	{
//	    text: 'Whoaaa, this is a lot of text that i couldn\'t predict',
//	    angle: 85,
//	    distance: 50,
//	    showAfter: 1000,
//	    hideAfter: 2000
//	}
//);
//}

//setup left menu

function setupLeftMenu() {
    $jquery("#section-menu")
        .accordion({
            "header": "a.menuitem"
        })
        .bind("accordionchangestart", function (e, data) {
            data.newHeader.next().andSelf().addClass("current");
            data.oldHeader.next().andSelf().removeClass("current");
        })
        .find("a.menuitem:first").addClass("current")
        .next().addClass("current");
		
		$jquery('#section-menu .submenu').css('height','auto');
}
