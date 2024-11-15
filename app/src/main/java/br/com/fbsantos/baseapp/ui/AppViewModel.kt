package br.com.fbsantos.baseapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.data.database.HistoricoAtividadeEntity
import br.com.fbsantos.baseapp.domain.service.PrivateUserService
import br.com.fbsantos.baseapp.domain.service.PublicAuthService
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.BuscarDeviceAuthUseCase
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.BuscarTemaUseCase
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.LimparConfiguracoesUseCase
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.SalvarDeviceAuthUseCase
import br.com.fbsantos.baseapp.domain.usecase.historicoatividades.BuscarUltimasAtividadesUseCase
import br.com.fbsantos.baseapp.domain.usecase.historicoatividades.LimparHistoricoAtividadesUseCase
import br.com.fbsantos.baseapp.domain.usecase.historicoatividades.SalvarHistoricoAtividadeUseCase
import br.com.fbsantos.baseapp.util.DateTimeHelper
import br.com.fbsantos.baseapp.util.ToastManager
import br.com.fbsantos.domain.usecase.configuracoes.SalvarTemaUseCase
import br.com.fbsantos.ui.app.AppUiState
import br.com.fbsantos.ui.app.model.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

class AppViewModel(
    private val buscarDeviceAuthUseCase: BuscarDeviceAuthUseCase,
    private val salvarDeviceAuthUseCase: SalvarDeviceAuthUseCase,
    private val limparConfiguracoesUseCase: LimparConfiguracoesUseCase,
    private val buscarTemaUseCase: BuscarTemaUseCase,
    private val salvarTemaUseCase: SalvarTemaUseCase,
    private val limparHistoricoAtividadesUseCase: LimparHistoricoAtividadesUseCase,
    private val buscarUltimasAtividadesUseCase: BuscarUltimasAtividadesUseCase,
    private val salvarHistoricoAtividadeUseCase: SalvarHistoricoAtividadeUseCase,
    private val publicAuthService: PublicAuthService,
    private val privateUserService: PrivateUserService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState

    init {
        viewModelScope.launch {
            setDeviceAuthEnabled(buscarDeviceAuthUseCase.execute().first() ?: false)
            setReadyDb(true)
        }
    }

    fun initializeSplashScreenLogin() {
        setReadyLogin(false) //nosso módulo de segurança exibe o SplashScren se isReadyLogin for false
    }

    fun initializeLogin() {
        viewModelScope.launch {
            try {
                if (privateUserService.isLoggedIn()) {
                    setLoggedIn(true)
                    setReadyLogin(true)
                } else {
                    setLoggedIn(false)
                    setReadyLogin(true)
                }
            } catch (e: Exception) {
                setLoggedIn(false)
                setReadyLogin(true)
                e.message?.let { ToastManager.show(it) }
            }
        }
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        if (_uiState.value.isLoggedIn == isLoggedIn) return

        _uiState.value = _uiState.value.copy(isLoggedIn = isLoggedIn)

        if (isLoggedIn) {
            initMe()
            initMenu()
            initTemaUsuario()
            loadHistoricoAtividade()
        }
    }

    fun initMe() {
        //preloader fajuto :)
        setNome("Carregando...")
        setSobrenome("")
        setEmail("Carregando...")
        setFotoBlob("")
        setUltimoAcesso(null)
        setCriacao(null)
        setAlteracao(null)

        viewModelScope.launch {
            try {
                val me = privateUserService.getMe()
                setNome(me.nome)
                setSobrenome(me.sobrenome)
                setEmail(me.email)
                setFotoBlob(me.photoBlob ?: "")
                setUltimoAcesso(DateTimeHelper.stringToDateTime(me.ultimoAcesso))
                setCriacao(DateTimeHelper.stringToDateTime(me.criadoEm))
                setAlteracao(DateTimeHelper.stringToDateTime(me.alteradoEm))
                setContaGoogle(me.isContaGoogle)
            } catch (e: Exception) {
                ToastManager.show(e.message ?: "Erro não determinado ao obter meus dados.")
            }
        }
    }

    fun initMenu() {
        setMenuItems(
            listOf(
                MenuItem(
                    "Início",
                    Icons.Default.Home,
                    Routes.Home.route
                ),
                MenuItem(
                    "Configurações",
                    Icons.Default.Settings,
                    Routes.Configuracoes.route
                ),
            )
        )
    }

    private fun initTemaUsuario() {
        viewModelScope.launch {
            setDarkTheme(buscarTemaUseCase.execute().first())
        }
    }

    fun alterarTemaUsuario(isDarkTheme: Boolean) {
        viewModelScope.launch {
            salvarTemaUseCase.execute(isDarkTheme)
            setDarkTheme(isDarkTheme)
        }
    }

    fun alterarBiometria(isBiometricsEnabled: Boolean) {
        viewModelScope.launch {
            salvarDeviceAuthUseCase.execute(isBiometricsEnabled)
            setDeviceAuthEnabled(isBiometricsEnabled)
        }
    }

    fun deslogar() {
        viewModelScope.launch {
            publicAuthService.logout()
            limparConfiguracoesUseCase.execute()
            limparHistoricoAtividadesUseCase.execute()
            setLoggedIn(false)
        }
    }

    fun setNome(nome: String) {
        _uiState.value = _uiState.value.copy(nome = nome)
    }

    fun setSobrenome(sobrenome: String) {
        _uiState.value = _uiState.value.copy(sobrenome = sobrenome)
    }

    fun setEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun setFotoBlob(fotoBlob: String) {
        _uiState.value = _uiState.value.copy(fotoBlob = fotoBlob)
    }

    fun setUltimoAcesso(ultimoAcesso: Date?) {
        _uiState.value = _uiState.value.copy(ultimoAcesso = ultimoAcesso)
    }

    fun getCriacao(): Date? {
        return _uiState.value.criacao
    }

    fun setCriacao(criacao: Date?) {
        _uiState.value = _uiState.value.copy(criacao = criacao)
    }

    fun getAlteracao(): Date? {
        return _uiState.value.alteracao
    }

    fun setAlteracao(alteracao: Date?) {
        val criacao = getCriacao()
        if (criacao != null && alteracao != null && criacao != alteracao) {
            _uiState.value = _uiState.value.copy(alteracao = alteracao)
        } else {
            _uiState.value = _uiState.value.copy(alteracao = null)
        }
    }

    fun setContaGoogle(isContaGoogle: Boolean) {
        _uiState.value = _uiState.value.copy(isContaGoogle = isContaGoogle)
    }

    fun setDarkTheme(isDarkTheme: Boolean?) {
        _uiState.value = _uiState.value.copy(isDarkTheme = isDarkTheme)
    }

    fun setMenuItems(menuItems: List<MenuItem>) {
        _uiState.value = _uiState.value.copy(menuItems = menuItems)
    }

    fun setDeviceAuthEnabled(isDeviceAuthEnabled: Boolean) {
        _uiState.value = _uiState.value.copy(isDeviceAuthEnabled = isDeviceAuthEnabled)
    }

    fun setReadyDb(isReadyDb: Boolean) {
        _uiState.value = _uiState.value.copy(isReadyDb = isReadyDb)
    }

    fun setReadyLogin(isReadyLogin: Boolean) {
        _uiState.value = _uiState.value.copy(isReadyLogin = isReadyLogin)
    }

    fun loadHistoricoAtividade() {
        viewModelScope.launch {
            setHistoricoAtividade(buscarUltimasAtividadesUseCase.execute())
        }
    }

    fun setHistoricoAtividade(historicoAtividade: List<HistoricoAtividadeEntity>) {
        _uiState.value = _uiState.value.copy(historicoAtividade = historicoAtividade)
    }

    fun addAtividade(
        rota: String?,
        descricao: String,
        icone: String
    ) {
        viewModelScope.launch {
            salvarHistoricoAtividadeUseCase.execute(rota, descricao, icone)
            loadHistoricoAtividade()
        }
    }
}