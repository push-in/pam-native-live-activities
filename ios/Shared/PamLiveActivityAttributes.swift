import ActivityKit
import Foundation
public struct PamLiveActivityAttributes:ActivityAttributes{public struct ContentState:Codable,Hashable{public let title:String;public let status:String;public let progress:Double;public let deepLink:String;public init(title:String,status:String,progress:Double,deepLink:String){self.title=title;self.status=status;self.progress=progress;self.deepLink=deepLink}};public let key:String;public init(key:String){self.key=key}}
