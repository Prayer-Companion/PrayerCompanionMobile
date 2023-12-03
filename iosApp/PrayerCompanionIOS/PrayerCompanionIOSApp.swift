//
//  PrayerCompanionIOSApp.swift
//  PrayerCompanionIOS
//
//  Created by Raed Ghazal on 21/09/2023.
//

import SwiftUI
import shared
import FirebaseCore

class AppDelegate: NSObject, UIApplicationDelegate {
   func application(_ application: UIApplication,
                    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
     FirebaseApp.configure()
       let b = NSLocale.current.language.languageCode?.identifier
       let a = Locale.preferredLanguages[0]
     return true
   }
 }

@main
struct PrayerCompanionIOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    init() {
        Main_iosKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
