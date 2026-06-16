using Microsoft.AspNetCore.SignalR;
using System.Collections.Concurrent;

namespace LabWork27.Server
{
    public class ChatHub : Hub
    {
        const string ServerName = "Сервер";

        private static readonly ConcurrentDictionary<string, string> ConnectionToRoom = new();
        private static readonly ConcurrentDictionary<string, string> ConnectionToUser = new();

        public async Task SendMessageToAll(string user, string message)
            => await Clients.All.SendAsync("ReceiveMessage", user, message);

        public async Task JoinRoom(string roomName, string userName)
        {
            var connectionId = Context.ConnectionId;

            ConnectionToRoom[connectionId] = roomName;
            ConnectionToUser[connectionId] = userName;

            await Groups.AddToGroupAsync(connectionId, roomName);

            await Clients.Group(roomName).SendAsync("ReceiveMessage", ServerName, $"{userName} 🔥🔥🔥ЖЁСКО ЗАЛЕТАЕТ В ЧАТ🔥🔥🔥.");
        }

        public async Task SendMessageToRoom(string message)
        {
            var connectionId = Context.ConnectionId;

            if (ConnectionToRoom.TryGetValue(connectionId, out var room) &&
                ConnectionToUser.TryGetValue(connectionId, out var user))
                await Clients.Group(room).SendAsync("ReceiveMessage", user, message);
        }

        public override async Task OnDisconnectedAsync(Exception? exception)
        {
            var connectionId = Context.ConnectionId;

            if (ConnectionToRoom.TryRemove(connectionId, out var room) &&
                ConnectionToUser.TryRemove(connectionId, out var user))
            {
                await Groups.RemoveFromGroupAsync(connectionId, room);
                await Clients.Group(room).SendAsync("ReceiveMessage", ServerName, $"{user} покинул чат.");
            }

            await base.OnDisconnectedAsync(exception);
        }
    }
}
