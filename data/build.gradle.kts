plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies {
//    Tink
    implementation("com.google.crypto.tink:tink-android:1.2.2")
//
//    implementation("androidx.room:room-ktx:2.6.1")
//    implementation("androidx.room:room-runtime:2.6.1")
//    annotationProcessor("androidx.room:room-compiler:2.6.1")
}