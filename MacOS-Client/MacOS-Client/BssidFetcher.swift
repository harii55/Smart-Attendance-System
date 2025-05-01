import Foundation
import CoreWLAN
enum BssidFetcher {
    static func getBSSID() -> String? {
         let wifiClient: CWWiFiClient = CWWiFiClient.shared()
         let interface = wifiClient.interface()
        let bssid = interface?.bssid()
        
        print("bssid from fetch: \(String(describing: bssid))")
        return bssid

    }
}
