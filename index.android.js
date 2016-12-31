import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  NativeModules,
  ScrollView
} from 'react-native';


export default class NativeModulesDemo extends Component {

  state={
    apiResponse:null
  }

  componentWillMount(){
    NativeModules.AndroidCallback.getEntitiesAsync("user",
      "uuid = 5a220204-ceb1-11e6-a734-122e0737977d",
      (error)=>{console.log(error)},
      (response)=>{this.setState({apiResponse:response})}
    )
  }

  render() {
    // console.log("countdown started")
    // NativeModules.AndroidCallback.startCountdown(5000,(error)=>{console.log(error)},(message)=>{console.log("Callback Executed: "+message)});

    return (
      <View style={styles.container}>
        <ScrollView>
          <Text style={styles.welcome}>
            Welcome to React Native!
          </Text>
          <Text >
            {this.state.apiResponse}
          </Text>
          <Text style={styles.instructions}>
            To get started, edit index.android.js
          </Text>
          <Text style={styles.instructions}>
            Double tap R on your keyboard to reload,{'\n'}
            Shake or press menu button for dev menu
          </Text>
        </ScrollView>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('NativeModulesDemo', () => NativeModulesDemo);
