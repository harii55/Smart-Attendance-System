//
//  WebSocketClient.swift
//  MacOS-Client
//
//  Created by Tanishq jain on 01/05/25.
//
import Foundation

class WebSocketClient {
    let email: String
    let bssid : String
    
    init(email: String, bssid: String) {
        self.email = email
        self.bssid = bssid
    }
    
    func connect() async {
        guard let url = URL(string: "ws://192.168.3.22:8080/attendance/wifi/ws") else {
            print("Invalid URL")
            return
        }
        
        let session = URLSession(configuration: .default)
        let task = session.webSocketTask(with: url)
        task.resume()
        
        print("Connected to \(bssid) as \(email)")
        
        while true {
            let payload = "{\"email\":\"\(email)\",\"bssid\":\"\(bssid)\"}"
            do {
                print("Sending sending")
                try await task.send(.string(payload))
                try await Task.sleep(nanoseconds: 5 * 1_000_000_000)
            }catch {
                print(
                    "WebSocket error: \(error), reconnecting..."
                )
                break
            }
        }
    }
}
