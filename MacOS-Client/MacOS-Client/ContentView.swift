//
//  ContentView.swift
//  MacOS-Client
//
//  Created by Tanishq jain on 01/05/25.
//

import SwiftUI
import SwiftData

struct ContentView: View {
    @State private var email: String = ""
    @State private var status: String = "Idle"
    
    var body: some View{
        VStack(spacing:16){
            Text("Smart Attendance Client")
                .font(.title)
                .padding(.top, 40)
            
            TextField("Enter your student email", text: $email)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding(.horizontal)
            
            Button("Start Attendance"){
                if email.isEmpty{
                    status = "Please enter your email"
                    return
                }
                
                status = "Connecting"
                
                Task{
                    let bssid = BssidFetcher.getBSSID() ?? "50-91-E3-57-78-FE"
                    if bssid == "UNAVAILABLE"{
                        status = "Unable to fetch BSSID"
                        return
                    }
                    
                    let client = WebSocketClient(email:email, bssid:bssid)
                    await client.connect()
                    status = "Attendance Started"
                }
            }.buttonStyle(DefaultButtonStyle())
            
            Text("Status: \(status)")
                .padding(.bottom, 40)
        }
        .frame(width: 400, height: 300)
    }
}

#Preview {
    ContentView()
        .modelContainer(for: Item.self, inMemory: true)
}
