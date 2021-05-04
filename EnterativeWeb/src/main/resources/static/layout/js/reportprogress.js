var source = null;
var response = null;

function initStream() {
    this.source = new EventSource('./accountbalancecheck/progress');

    this.source.onmessage = function (event) {
        var response = JSON.parse(event.data);
        var lastResponse = this.response || response;
        var progressDiff = response.progress - lastResponse.progress;
        var updateDiff = moment(response.lastUpdate).diff(lastResponse.lastUpdate);
        var progressPerTime = updateDiff / (progressDiff || 1);
        var toBeDone = response.progressMax - response.progress;
        var forecast = Math.round((progressPerTime * toBeDone) / 1000); // seconds
        var forecastAsText = "";

        if (forecast < 100) {
            forecastAsText = `${forecast} segundos`;
        } else if (forecast < 3600) {
            forecastAsText = `${Math.round(forecast / 60)} minutos`;
        }

        var percentage = Math.round((response.progress * 100) / response.progressMax);

        var overview = "<div class='row'>"+
                       "     <div class='col-12'>"+
                       "         <div class='progress' style='height: 50px;'>"+
                       "             <div class='progress-bar' role='progressbar' style='width: " + percentage + "%'>"+
                       "                 " + response.progress + " de " + response.progressMax + " (" + percentage + "%)"+
                       "             </div>"+
                       "         </div>"+
                       "     </div>"+
                       " </div>"+
                       " <div class='row'>"+
                       "     <div class='col-6'>ETA: " + forecastAsText + "</div>"+
                       "     <div class='col-6'>" + moment(response.lastUpdate).format('DD/MM/YYYY HH:mm:ss') + "</div>"+
                       "</div>";
        $("#divOverview").html(overview);

        response.messages.forEach((m) => {
            var html = "<div class='row'><div class='col-12 loginfo'>" + m + "</div></div>";
            $("#divLog").append(html);
        });

        while ($("#divLog").children().length > 15) {
            $("#divLog").children().first().remove();
        }

        this.response = response;
    }
}