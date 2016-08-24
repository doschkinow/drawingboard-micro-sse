'use strict';

/* Controllers */

// Controller for the main page (list of drawings)
function MainController($scope, DrawingService, WssService) {
    // get location of WS service
    $scope.location = WssService.get();
    // obtain drawings from the RESTful service
    $scope.drawings = DrawingService.query();

    // deletes a single drawing by ID
    $scope.remove = function ($drawingId) {
        DrawingService.remove({drawingId: $drawingId});
    };

    // adds a new drawing
    $scope.addDrawing = function () {
        var newDrawing = new DrawingService({name: $scope.drawingName});
        $scope.drawingName = '';
        newDrawing.$save();
    };
    
    var protocol = document.location.protocol.includes('https') ? 'wss' : 'ws';
    $scope.websocket = new WebSocket(protocol + "://" + document.location.host + "/websockets");
    $scope.websocket.onmessage = function (evt) {
        console.log(evt.data);
        $scope.drawings = DrawingService.query();
    };



    // clean up
    $scope.$on("$destroy", function (event) {

        // sometimes when this function is called, the websocket is already closed
        if ($scope.websocket.readyState > 0)
            $scope.websocket.close();
    });
}

