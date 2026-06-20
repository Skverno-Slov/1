package com.example.labwork30

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.labwork30.ui.theme.LabWork30Theme
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabWork30Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Create, "") },
                    label = { Text("Заметки") },
                    selected = currentRoute == "notes",
                    onClick = {
                        if (currentRoute != "notes") {
                            navController.navigate("notes") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountCircle, "") },
                    label = { Text("Вход") },
                    selected = currentRoute == "eula_register",
                    onClick = {
                        if (currentRoute != "eula_register") {
                            navController.navigate("eula_register") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Email, "") },
                    label = { Text("Блакнот") },
                    selected = currentRoute == "external_files",
                    onClick = {
                        if (currentRoute != "external_files") {
                            navController.navigate("external_files") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "eula_register",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("eula_register") {
                EulaAndRegisterScreen()
            }
            composable("notes") {
                NotesScreen()
            }
            composable("external_files") {
                ExternalFilesScreen()
            }
        }
    }
}

fun isPasswordWeak(context: Context, password: String): Boolean {
    if (password.isEmpty()) return false
    context.assets.open("weak_passwords.txt").use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (line?.trim() == password) {
                    return true
                }
            }
        }
    }
    return false
}

@Composable
fun EulaAndRegisterScreen() {
    val context = LocalContext.current

    val eulaText = remember {
        runCatching {
            context.assets.open("eula.txt").bufferedReader().use { it.readText() }
        }.getOrDefault("Ошибка загрузки") }

    var isChecked by remember { mutableStateOf(false) }
    var isEulaAccepted by remember { mutableStateOf(false) }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isWeakPassword by remember { mutableStateOf(false) }

    if (!isEulaAccepted) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = eulaText,
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
                Text("Я прочитал условия лицензионного соглашения")
            }
            Button(
                onClick = { isEulaAccepted = true },
                enabled = isChecked,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Принять")
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = login,
                onValueChange = { login = it },
                placeholder = { Text("Логин") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = password,
                onValueChange = {
                    password = it
                    isWeakPassword = isPasswordWeak(context, it)
                },
                placeholder = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                isError = isWeakPassword
            )
            if (isWeakPassword) {
                Text(
                    text = "Пароль слишком простой",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Button({}) {
                Text("Войти")
            }
        }
    }
}

@Composable
fun NotesScreen() {
    val context = LocalContext.current

    val notesDir = remember { File(context.filesDir, "app_notes").apply { if (!exists()) mkdirs() } }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var refreshTrigger by remember { mutableStateOf(0) }

    val notesFiles = remember(refreshTrigger) {
        notesDir.listFiles()?.filter { it.isFile && it.extension == "txt" } ?: emptyList()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = content,
            onValueChange = { content = it },
            placeholder = { Text("Текст...") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (title.isNotBlank()) {
                    val noteFile = File(notesDir, "${title.trim()}.txt")
                    noteFile.writeText(content)
                    title = ""
                    content = ""
                    refreshTrigger++
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(notesFiles) { file ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            title = file.nameWithoutExtension
                            content = file.readText()
                        }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = file.nameWithoutExtension, modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        file.delete()
                        refreshTrigger++
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                    }
                }
            }
        }
    }
}

@Composable
fun ExternalFilesScreen() {
    val context = LocalContext.current
    var fileContent by remember { mutableStateOf("") }

    val openDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { inputStream ->
                fileContent = inputStream.bufferedReader().use { reader -> reader.readText() }
            }
        }
    }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                outputStream.bufferedWriter().use { writer -> writer.write(fileContent) }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = fileContent,
            onValueChange = { fileContent = it },
            placeholder = { Text("Текст...") },
            modifier = Modifier.fillMaxWidth().weight(1f),
            minLines = 5
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                openDocumentLauncher.launch("text/plain")
            }) {
                Text("Открыть")
            }

            Button(onClick = {
                createDocumentLauncher.launch("newFile.txt")
            }) {
                Text("Сохранить")
            }
        }
    }
}