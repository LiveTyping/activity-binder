# LiveTyping Binder

![alt text](https://2016.gdg-siberia.com/img/partners/livetyping.png)

This library provides you an easy way to get permissions from user and to login (or sign up) via the most common social networks.

### Permissions 

There are 3 type of permissions:
* Passive permissions 
* Active permissions
* Global permissions

Passive permissions are permissions which get asked only once.

Active permissions are permissions which get asked every time until user allows their. But if user sets option "don't show it again", the library will ask him to go to the settings of the application.

Global permissions are necessary in cases when without permissions working of application doesn't make sense.

### Social networks
You can log in (or sign up) via 3 social networks:
* [VK](http://vk.com)
* [Facebook](https://facebook.com/)
* [Instagram](http://instagram.com)
* [Google](http://google.com)

The library provides 2 opportunities to log in (sign up)  via Google account by using tokenId or accessToken.


## Getting Started
The first step is to add the JitPack repository to your build file
```Gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

The second step is to include LiveTyping Binder into your project. 

Please replace ```$latest_version``` with the latest version numbers  [![](https://jitpack.io/v/LiveTyping/activity-binder.svg)](https://jitpack.io/#LiveTyping/activity-binder)


### Permissions 
You have to include core to follow the life cycle of activity
```gradle
implementation "com.github.LiveTyping.activity-binder:core:$latest_version"
```
Getting permissions:
```
implementation "com.github.LiveTyping.activity-binder:permission:$latest_version"
```

### Social Networks
You have to include login core to follow the life cycle of activity
```
implementation "com.github.LiveTyping.activity-binder:logincore:$latest_version"
```
Then you can get social network to log in or sign up:
```
implementation "com.github.LiveTyping.activity-binder:socialnetwork:$latest_version"
```
Instead of ```socialnetwork```, please, use a name of approptiate social network: vk, facebook, instagram or google

## Usage

#### Step 1 - Following the life cycle
You should create variables of ```PermissionBinder``` and ```SocialLoginBinder``` classes to follow the life cycle of activity:
```Kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var permissionBinder: PermissionBinder
    private lateinit var socialLoginBinder: SocialLoginBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Rest of code
        permissionBinder = PermissionBinder()
        socialLoginBinder = SocialLoginBinder()        
        //Rest of code
    }
    
    override fun onStart() {
        permissionBinder.attach(this)
        socialLoginBinder.attach(this)
        super.onStart()
    }

    override fun onStop() {
        permissionBinder.detach(this)
        socialLoginBinder.attach(this)
        super.onStop()
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        permissionBinder.onActivityResult(requestCode, data, this)
        socialLoginBinder.onActivityResult(requestCode, data, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionBinder.onRequestPermissionResult(requestCode, grantResults)
        socialLoginBinder.onActivityResult(requestCode, data, this)
    }
   //Rest of code 
}
``` 
#### Step 2
* ##### Getting permission
```Kotlin
permissionBinder.activePermission(Manifest.permission.CAMERA, permissionMessage){ granted->
    if (granted)
        //Do something if permission of using camera was allowed
    else
       //Do something if permission of using camera was denied
```

* ##### Log in via VK
```Kotlin
socialLoginBinder.loginWith(VkNetwork()) { vkLoginResult->
            sendTokenAndEmailToServer(vkLoginResult.accessToken, vkLoginResult.email) }
        }
```
## License
```
Copyright 2018 Dmitry Alexeeenkoff.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
