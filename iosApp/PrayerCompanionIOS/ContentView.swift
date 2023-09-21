//
//  ContentView.swift
//  PrayerCompanionIOS
//
//  Created by Raed Ghazal on 21/09/2023.
//

import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
            Text(Greeting().greet() + " " + AppLanguage.ar.name)
        }
        .padding()
    }
}

#Preview {
    ContentView()
}
