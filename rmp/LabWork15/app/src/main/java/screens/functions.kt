package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun AuthorizationScreen(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "",
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF6200EE)
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = login,
            onValueChange = { login = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Логин") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Пароль") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToProfile,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Войти", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreen(onNavigateToAuth: () -> Unit = {}) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "",
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF03DAC5)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Регистрация", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = login, onValueChange = { login = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Логин") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = password, onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = confirmPassword, onValueChange = { confirmPassword = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Подтверждение пароля") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = phone, onValueChange = { phone = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("+7 (xxx) xxx xxx xx-xx)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = email, onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("example@mail.com") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = age, onValueChange = { age = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Возраст") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToAuth,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03DAC5))
        ) {
            Text("ОК", fontSize = 16.sp, color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreen(onNavigateToAuth: () -> Unit = {}) {
    val aboutMeText = "Дед помыл собаку, она блестела от чистоты, а я взял банку мёда, литров 10, и начал поливать собаку. Она из чёрной превратилась в желтую, потом я взял и начал кидаться медом в стены, там шлепок такой был прикольный, замазал все стекла, вся терраса была в меду, но мне было мало, и я налил меда в резиновые галоши деда. Когда дед вернулся домой, собака уже засохла нафиг, стекла стали тонированными, а в галошах поселились муравьи. Так меня в жизни никогда не били. Целый год называли неблагодарным. Дед помыл собаку, она блестела от чистоты, а я взял банку мёда, литров 10, и начал поливать собаку. Она из чёрной превратилась в желтую, потом я взял и начал кидаться медом в стены, там шлепок такой был прикольный, замазал все стекла, вся терраса была в меду, но мне было мало, и я налил меда в резиновые галоши деда. Когда дед вернулся домой, собака уже засохла нафиг, стекла стали тонированными, а в галошах поселились муравьи. Так меня в жизни никогда не били. Целый год называли неблагодарным. Дед помыл собаку, она блестела от чистоты, а я взял банку мёда, литров 10, и начал поливать собаку. Она из чёрной превратилась в желтую, потом я взял и начал кидаться медом в стены, там шлепок такой был прикольный, замазал все стекла, вся терраса была в меду, но мне было мало, и я налил меда в резиновые галоши деда. Когда дед вернулся домой, собака уже засохла нафиг, стекла стали тонированными, а в галошах поселились муравьи. Так меня в жизни никогда не били. Целый год называли неблагодарным. Дед помыл собаку, она блестела от чистоты, а я взял банку мёда, литров 10, и начал поливать собаку. Она из чёрной превратилась в желтую, потом я взял и начал кидаться медом в стены, там шлепок такой был прикольный, замазал все стекла, вся терраса была в меду, но мне было мало, и я налил меда в резиновые галоши деда. Когда дед вернулся домой, собака уже засохла нафиг, стекла стали тонированными, а в галошах поселились муравьи. Так меня в жизни никогда не били. Целый год называли неблагодарным. Дед помыл собаку, она блестела от чистоты, а я взял банку мёда, литров 10, и начал поливать собаку. Она из чёрной превратилась в желтую, потом я взял и начал кидаться медом в стены, там шлепок такой был прикольный, замазал все стекла, вся терраса была в меду, но мне было мало, и я налил меда в резиновые галоши деда. Когда дед вернулся домой, собака уже засохла нафиг, стекла стали тонированными, а в галошах поселились муравьи. Так меня в жизни никогда не били. Целый год называли неблагодарным. Дед помыл собаку, она блестела от чистоты, а я взял банку мёда, литров 10, и начал поливать собаку. Она из чёрной превратилась в желтую, потом я взял и начал кидаться медом в стены, там шлепок такой был прикольный, замазал все стекла, вся терраса была в меду, но мне было мало, и я налил меда в резиновые галоши деда. Когда дед вернулся домой, собака уже засохла нафиг, стекла стали тонированными, а в галошах поселились муравьи. Так меня в жизни никогда не били. Целый год называли неблагодарным. Дед помыл собаку, она блестела от чистоты, а я взял банку мёда, литров 10, и начал поливать собаку. Она из чёрной превратилась в желтую, потом я взял и начал кидаться медом в стены, там шлепок такой был прикольный, замазал все стекла, вся терраса была в меду, но мне было мало, и я налил меда в резиновые галоши деда. Когда дед вернулся домой, собака уже засохла нафиг, стекла стали тонированными, а в галошах поселились муравьи. Так меня в жизни никогда не били. Целый год называли неблагодарным. Дед помыл собаку, она блестела от чистоты, а я взял банку мёда, литров 10, и начал поливать собаку. Она из чёрной превратилась в желтую, потом я взял и начал кидаться медом в стены, там шлепок такой был прикольный, замазал все стекла, вся терраса была в меду, но мне было мало, и я налил меда в резиновые галоши деда. Когда дед вернулся домой, собака уже засохла нафиг, стекла стали тонированными, а в галошах поселились муравьи. Так меня в жизни никогда не били. Целый год называли неблагодарным. Дед помыл собаку, она блестела от чистоты, а я взял банку мёда, литров 10, и начал поливать собаку. Она из чёрной превратилась в желтую, потом я взял и начал кидаться медом в стены, там шлепок такой был прикольный, замазал все стекла, вся терраса была в меду, но мне было мало, и я налил меда в резиновые галоши деда. Когда дед вернулся домой, собака уже засохла нафиг, стекла стали тонированными, а в галошах поселились муравьи. Так меня в жизни никогда не били. Целый год называли неблагодарным. Дед помыл собаку, она блестела от чистоты, а я взял банку мёда, литров 10, и начал поливать собаку. Она из чёрной превратилась в желтую, потом я взял и начал кидаться медом в стены, там шлепок такой был прикольный, замазал все стекла, вся терраса была в меду, но мне было мало, и я налил меда в резиновые галоши деда. Когда дед вернулся домой, собака уже засохла нафиг, стекла стали тонированными, а в галошах поселились муравьи. Так меня в жизни никогда не били. Целый год называли неблагодарным"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Профиль",
            modifier = Modifier.size(80.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Профиль", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            ProfileItem(label = "Логин:", value = "android_demon")
            ProfileItem(label = "Имя:", value = "Игорь")
            ProfileItem(label = "Возраст:", value = "25")
            ProfileItem(label = "Эл. почта:", value = "android_demon@example.com")

            Spacer(modifier = Modifier.height(12.dp))
            Text("О себе:", fontSize = 14.sp, color = Color.Gray)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = aboutMeText, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNavigateToAuth,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Text("Назад", fontSize = 16.sp, color = Color.Black)
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(100.dp))
        Text(text = value, color = Color.DarkGray)
    }
}