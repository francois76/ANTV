//
//  VideoPlayerView.swift
//  iosApp
//
//  Created by François Gognet on 15/07/2023.
//  Copyright © 2023 fr.fgognet. All rights reserved.
//

import SwiftUI
import AVKit

struct VideoPlayerView: View {
    var body: some View {
        VideoPlayer(player: AVPlayer(url:  URL(string: "https://bit.ly/swswift")!)) {
            VStack {
                Text("Watermark")
                    .foregroundStyle(.black)
                    .background(.white.opacity(0.7))
                Spacer()
            }
            .frame(width: 400, height: 300)
        }
    }
}

struct VideoPlayerView_Previews: PreviewProvider {
    static var previews: some View {
        VideoPlayerView()
    }
}
