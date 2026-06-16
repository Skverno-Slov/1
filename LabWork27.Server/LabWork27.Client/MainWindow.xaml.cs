using Microsoft.AspNetCore.SignalR.Client;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace LabWork27.Client
{
    public partial class MainWindow : Window
    {
        private HubConnection _connection;

        public MainWindow()
        {
            InitializeComponent();
        }

        private async void LoginButton_Click(object sender, RoutedEventArgs e)
        {
            string userName = UserNameInput.Text.Trim();
            string roomName = RoomNameInput.Text.Trim();

            if (string.IsNullOrEmpty(userName) || string.IsNullOrEmpty(roomName))
            {
                MessageBox.Show("Заполните все поля");
                return;
            }

            _connection = new HubConnectionBuilder()
                .WithUrl("http://localhost:5073/chat") 
                .WithAutomaticReconnect()
                .Build();

            _connection.On<string, string>("ReceiveMessage", (user, message) =>
            {
                Dispatcher.Invoke(() =>
                {
                    MessagesList.Items.Add($"[{user}]: {message}");
                    MessagesList.ScrollIntoView(MessagesList.Items[MessagesList.Items.Count - 1]);
                });
            });

            try
            {
                await _connection.StartAsync();

                await _connection.InvokeAsync("JoinRoom", roomName, userName);

                LoginPanel.Visibility = Visibility.Collapsed;
                ChatPanel.Visibility = Visibility.Visible;
                ChatName.Text = $" {roomName} | {userName}";
            }
            catch (Exception ex)
            {
                MessagesList.Items.Add($"Ошибка:\n {ex.Message}");
            }
        }

        private async void SendButton_Click(object sender, RoutedEventArgs e)
        {
            string message = MessageInput.Text.Trim();

            if (string.IsNullOrEmpty(message)) return;

            try
            {
                await _connection.InvokeAsync("SendMessageToRoom", message);
                MessageInput.Clear();
            }
            catch (Exception ex)
            {
                MessagesList.Items.Add($"Ошибка:\n {ex.Message}");
            }
        }
    }
}