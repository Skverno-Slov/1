using Microsoft.Win32;
using System.Management;
using System.Net.NetworkInformation;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;

namespace LabWork21
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();

            LoadWmiInfo("Win32_Processor", ProcessorList, ["Name", "NumberOfCores", "MaxClockSpeed"]);
            LoadWmiInfo("Win32_VideoController", VideoList, ["Name", "AdapterRAM", "DriverVersion"]);
            LoadWmiInfo("Win32_MotherboardDevice", MotherboardList, ["Product", "Manufacturer", "SerialNumber"]);
            LoadWmiInfo("Win32_DiskDrive", DiskList, ["Model", "Size", "InterfaceType"]);
            LoadWmiInfo("Win32_OperatingSystem", OsList, ["Caption", "Version", "OSArchitecture"]);

            LoadNetworkInfo();

            LoadInstalledPrograms();
        }

        private void LoadWmiInfo(string wmiClass, ListBox list, string[] properties)
        {
            try
            {
                using (ManagementObjectSearcher searcher = new($"SELECT * FROM {wmiClass}"))
                {
                    int deviceCount = 1;
                    foreach (ManagementObject managmentObject in searcher.Get())
                    {
                        var text = $"Устройство {deviceCount++}\n";

                        foreach (string prop in properties)
                        {
                            string val = managmentObject[prop]?.ToString() ?? "Нет данных";
                            text += $"{prop}: {val}\n";
                        }

                        list.Items.Add(text);
                    }
                }
            }
            catch (Exception ex)
            {
                list.Items.Add($"Ошибка: {ex.Message}");
            }
        }

        private void LoadNetworkInfo()
        {
            try
            {
                int adapterCount = 1;
                foreach (NetworkInterface ni in NetworkInterface.GetAllNetworkInterfaces())
                {
                    var text = $"Адаптер #{adapterCount++}: {ni.Name}";

                    text += $"Описание: {ni.Description}\n";
                    text += $"Тип: {ni.NetworkInterfaceType}\n";
                    text += $"Статус: {ni.OperationalStatus}\n";
                    text += $"Скорость: {ni.Speed / 1000000}\n";

                    NetworkList.Items.Add(text);
                }
            }
            catch (Exception ex)
            {
                NetworkList.Items.Add($"Ошибка: {ex.Message}");
            }
        }

        private void LoadInstalledPrograms()
        {
            string[] registryPaths =
            [
                @"Software\Microsoft\Windows\CurrentVersion\Uninstall",
                @"SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall",
                @"SOFTWARE\Wow6432Node\Microsoft\Windows\CurrentVersion\Uninstall"
            ];

            ReadProgramsFromRoot(Registry.CurrentUser, registryPaths);

            ReadProgramsFromRoot(Registry.LocalMachine, registryPaths);
        }

        private void ReadProgramsFromRoot(RegistryKey rootKey, string[] paths)
        {
            foreach (string path in paths)
            {
                using (RegistryKey? key = rootKey.OpenSubKey(path))
                {
                    if (key is null) 
                        continue;

                    foreach (string subkeyName in key.GetSubKeyNames())
                    {
                        using (RegistryKey? subkey = key.OpenSubKey(subkeyName))
                        {
                            string? displayName = subkey?.GetValue("DisplayName")?.ToString();

                            if (!String.IsNullOrWhiteSpace(displayName) && !ProgramsListBox.Items.Contains(displayName))
                                ProgramsListBox.Items.Add(displayName);
                        }
                    }
                }
            }
        }
    }
}