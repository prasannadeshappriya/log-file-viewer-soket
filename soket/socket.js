/**
 * Created by prasanna_d on 9/13/2017.
 */
'use strict';
angular.module('socket_app')
    .factory('socket', function (LoopBackAuth) {
        var socket = io.connect('http://localhost:3000');
        var id = LoopBackAuth.accessTokenId;
        var userId = LoopBackAuth.currentUserId;
        socket.on('connect', function(){
            socket.emit('authentication', {id: id, userId: userId });
            socket.on('authenticated', function() {
                // use the socket as usual
                console.log('User is authenticated');
            });
        });
        return socket;
    });