//
//  ContentView.swift
//  Shared
//
//  Created by François Gognet on 13/08/2022.
//

import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        let tabs = [
            0:shared.AbstractRouteKt.getRoute(routeId: shared.Routes.live).unsafelyUnwrapped,
            1:shared.AbstractRouteKt.getRoute(routeId: shared.Routes.playlist).unsafelyUnwrapped,
            2:shared.AbstractRouteKt.getRoute(routeId: shared.Routes.search).unsafelyUnwrapped]

        TabView {
            ForEach(Array(tabs.keys.sorted()), id:\.self) {  key in
                Text("The content of the first view")
                    .tabItem {
                        Image(systemName: "phone.fill")
                        Text(tabs[key].unsafelyUnwrapped.nameID.unsafelyUnwrapped.resourceId)
                    }
            }
            
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
