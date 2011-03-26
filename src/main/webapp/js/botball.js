function DrawCanvas(canvasName) {
    canvas = document.getElementById(canvasName);
    this.context = canvas.getContext("2d"); //Note we assume browser support!
}

DrawCanvas.prototype.drawRobot = function(x, y, w, h, angle) {
    this.context.save();
    this.context.rotate(angle)
    this.context.fillStyle = "rgb(20,0,0)";
    this.context.fillRect(x, y, w, h);
    this.context.restore();
}

function Simulation() {
    this.interval = null
}

Simulation.prototype.start = function() {

    if (this.interval == null) {
        this.sendStartSimulation();
        this.interval = setInterval("updateScene()", 1000);
    }
}

Simulation.prototype.stop = function() {

    if (this.interval != null) {
        clearInterval(this.interval);
        this.sendStopSimulation();
        this.interval = null;
    }
}

Simulation.prototype.sendStartSimulation = function() {
    $.ajax({
        url:"simulation/start",
        dataType:"json",
        success:function(data) {
            $("#messages").html(data.message)
        }
    });
}

Simulation.prototype.sendStopSimulation = function() {
    $.ajax({
        url:"simulation/stop",
        dataType:"json",
        success:function(data) {
            $("#messages").html(data.message)
        }
    });
}

function handleMessage(msg) {
    $("#simulationStatus").html(msg.message)
}

function drawRobot(robot) {

    var x = robot.position[0];
    var y = robot.position[1];

    var width = robot.boundingBox[0];
    var height = robot.boundingBox[0];

    var rotation = robot.rotation;
    var rotationRadians = (3.14 * rotation/180.0);

    draw.drawRobot(x, y, width, height, rotationRadians);
}

function loadRobotJSON(data) {
    if ($.isArray(data.robots)) {
        for(var i =0; i < data.robots.length; i++) {
            drawRobot(data.robots[i]);
        }

        $("#messages").html("Loaded robots!")

    } else {
        $("#messages").html("Unable to open robots response!")
    }
}

function updateScene() {

    $.ajax({
        url:"simulation/scene/robots",
        dataType:"json",
        success: loadRobotJSON
    });
}