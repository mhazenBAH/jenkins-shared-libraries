node {
  docker.image("gradle:6.0.0").inside() {
    stage('Checkout') {
      git 'https://github.com/mhazenBAH/RestaurantList'
    }
    stage('Build') {
      sh 'gradle clean build'
    }
	stage('Test') {
	  sh 'gradle test --tests RestaurantListApplicationTests'
	}
    stage('Code Quality') {
      sh 'gradle sonarqube -Dsonar.projectName=RestaurantList -Dsonar.projectKey=RestaurantList  -Dsonar.host.url=http://192.168.4.86:9000  -Dsonar.login=0ba69a8973b8a28d0db2535d27536861ea0c5068'
    }
  }
  stage('Build Image') {
    docker.withRegistry('http://localhost:5000/') {
      git 'https://github.com/mhazenBAH/RestaurantList'
      docker.build('restaurant-list').push('latest')
    }
  }
  /*stage('Run Container') {
      sh 'docker run -d --name restaurant-list -p 9001:9001 restaurant-list:latest'
  }*/
}
