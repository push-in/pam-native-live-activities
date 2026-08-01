import ActivityKit
import SwiftUI
import WidgetKit

struct PamLiveActivityWidget: Widget {
    var body: some WidgetConfiguration {
        ActivityConfiguration(for: PamLiveActivityAttributes.self) { context in
            content(context.state)
                .widgetURL(URL(string: context.state.deepLink))
        } dynamicIsland: { context in
            DynamicIsland {
                DynamicIslandExpandedRegion(.leading) {
                    Text(context.state.title).bold()
                }
                DynamicIslandExpandedRegion(.trailing) {
                    Text("\(Int(context.state.progress * 100))%").monospacedDigit()
                }
                DynamicIslandExpandedRegion(.bottom) {
                    ProgressView(value: context.state.progress)
                    Text(context.state.status).font(.caption)
                }
            } compactLeading: {
                Text("P")
            } compactTrailing: {
                Text("\(Int(context.state.progress * 100))")
            } minimal: {
                ProgressView(value: context.state.progress)
            }
        }
    }

    private func content(_ state: PamLiveActivityAttributes.ContentState) -> some View {
        VStack(alignment: .leading) {
            Text(state.title).bold()
            Text(state.status).font(.caption)
            ProgressView(value: state.progress)
        }
        .padding()
    }
}

@main
struct PamLiveActivityBundle: WidgetBundle {
    var body: some Widget {
        PamLiveActivityWidget()
    }
}
