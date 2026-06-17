using System.Security.Cryptography;

namespace LabWork19_1
{
    internal class Program
    {
        static void Main(string[] args)
        {
            if (args.Length < 3)
            {
                return;
            }

            string targetDir = args[0];
            bool searchRecursive = args[1].ToLower() == "all";
            bool deleteDuplicates = args[2].ToLower() == "delete";

            if (!Directory.Exists(targetDir))
            {
                Console.WriteLine($"Ошибка: Каталог '{targetDir}' не существует.");
                return;
            }

            SearchOption searchOption = searchRecursive ? SearchOption.AllDirectories : SearchOption.TopDirectoryOnly;

            string[] files = Directory.GetFiles(targetDir, "*.*", searchOption);

            Dictionary<string, List<string>> hashDictionary = new();

            foreach (string file in files)
            {
                try
                {
                    string fileHash = CalculateHash(file);

                    if (!hashDictionary.ContainsKey(fileHash))
                        hashDictionary[fileHash] = new List<string>();
                    hashDictionary[fileHash].Add(file);
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Ошибка: {ex.Message}");
                }
            }

            bool duplicatesFound = false;

            foreach (var hash in hashDictionary)
            {
                if (hash.Value.Count > 1)
                {
                    duplicatesFound = true;

                    string original = hash.Value[0];

                    for (int i = 1; i < hash.Value.Count; i++)
                    {
                        string duplicatePath = hash.Value[i];

                        Console.WriteLine($"Дубликат: {duplicatePath}");

                        if (deleteDuplicates)
                        {
                            try
                            {
                                File.Delete(duplicatePath);
                            }
                            catch (Exception ex)
                            {
                                Console.WriteLine($"Ошибка: {ex.Message}");
                            }
                        }
                    }
                }
            }

            if (!duplicatesFound)
                Console.WriteLine("Дубликаты не обнаружены.");

            Console.ReadKey();
        }

        static string CalculateHash(string filename)
        {
            using (var md5 = MD5.Create())
            {
                using (var stream = File.OpenRead(filename))
                {
                    byte[] hash = md5.ComputeHash(stream);
                    return BitConverter.ToString(hash).Replace("-", "").ToLowerInvariant();
                }
            }
        }
    }
}
