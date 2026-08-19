package com.mountsa.fm2027.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mountsa.fm2027.viewmodel.OnboardingViewModel

@Composable
fun ManagerProfileScreen(
    viewModel: OnboardingViewModel, 
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    var name by remember { mutableStateOf(profile.name) }
    var age by remember { mutableStateOf(if (profile.age == 0) "" else profile.age.toString()) }
    var selectedAvatarUri by remember { mutableStateOf<Uri?>(profile.avatarUri?.let { Uri.parse(it) }) }
    
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val fmGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2ECC71), Color(0xFF27AE60))
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                selectedAvatarUri = uri
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B13))
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color(0xFF2ECC71), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("FM", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("MANAGER SETUP 2027", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("SEASON PROFILE INITIALIZATION", color = Color(0xFF2ECC71), fontSize = 7.sp, letterSpacing = 0.5.sp)
            }
        }

        // Main Content (Centered and Compact)
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar Selection
            Box(
                modifier = Modifier.size(110.dp),
                contentAlignment = Alignment.Center
            ) {
                // Main Profile Image Container
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .shadow(12.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFF0A141D))
                        .border(2.dp, Color(0xFF2ECC71).copy(alpha = 0.3f), CircleShape)
                        .clickable {
                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedAvatarUri != null) {
                        AsyncImage(
                            model = selectedAvatarUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White.copy(0.3f),
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }

                // Small "+" Icon at Bottom Right - Only show if no photo is selected
                if (selectedAvatarUri == null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-4).dp, y = (-4).dp)
                            .size(32.dp)
                            .background(Color(0xFF2ECC71), CircleShape)
                            .border(3.dp, Color(0xFF0A141D), CircleShape)
                            .clickable {
                                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Import Photo",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "MANAGER IDENTITY",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            // Inputs Container
            Column(
                modifier = Modifier
                    .width(340.dp)
                    .background(Color(0xFF0A141D), RoundedCornerShape(8.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfileInputField(
                    label = "MANAGER DESIGNATION",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Enter your name",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
                
                ProfileInputField(
                    label = "BIOLOGICAL (AGE)",
                    value = age,
                    onValueChange = { 
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            if (it.length <= 2) age = it
                        }
                    },
                    placeholder = "25",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }
        }

        // Bottom Navigation
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            // Back Button (Bottom Left)
            Button(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .height(32.dp)
                    .width(90.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.05f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text("BACK", color = Color.White.copy(alpha = 0.6f), fontWeight = FontWeight.Bold, fontSize = 9.sp)
            }

            // Next Button (Bottom Right)
            Button(
                onClick = {
                    viewModel.updateProfile(name, age.toIntOrNull() ?: 0, 0, selectedAvatarUri?.toString())
                    onNext()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .height(32.dp)
                    .width(130.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(fmGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("NEXT CYCLE", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column {
        Text(
            text = label, 
            color = Color.Gray, 
            fontSize = 9.sp, 
            fontWeight = FontWeight.Bold, 
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 14.sp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(4.dp))
                .padding(10.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(placeholder, color = Color.White.copy(alpha = 0.2f), fontSize = 14.sp)
                    }
                    innerTextField()
                }
            }
        )
    }
}
