package com.prayercompanion.shared.data.repositories

//actual class AuthenticationRepositoryImplX constructor(
//    private val googleSignInClient: GoogleSignInClient,
//    private val dataStoresRepo: DataStoresRepo,
//) : AuthenticationRepository {
//
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//
//    actual override suspend fun isSignedIn(): Boolean  {
//        val user = FirebaseAuth.getInstance().currentUser
//
//        // if the user object is not null means they are signed in
//        dataStoresRepo.updateAppPreferencesDataStore {
//            it.copy(isSignedIn = user != null)
//        }
//        return user != null
//    }
//
//    actual override fun signInWithGoogle(
//        token: String,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        val credential = GoogleAuthProvider.getCredential(token, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener { task ->
//                onSignInComplete(task, onSuccess, onFailure)
//            }
//    }
//
//    actual override fun signInAnonymously(
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        auth.signInAnonymously()
//            .addOnCompleteListener { task ->
//                onSignInComplete(task, onSuccess, onFailure)
//            }
//    }
//
//    private fun onSignInComplete(
//        task: Task<AuthResult>,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        if (task.isSuccessful) {
//            CoroutineScope(Dispatchers.Default).launch {
//                dataStoresRepo.updateAppPreferencesDataStore {
//                    it.copy(isSignedIn = true)
//                }
//            }
//            onSuccess()
//        } else {
//            signOut()
//            task.exception?.let {
//                onFailure(it)
//                it.printStackTraceInDebug()
//            }
//        }
//    }
//
//    private fun signOut() {
//        auth.signOut()
//        googleSignInClient.signOut()
//    }
//
//}