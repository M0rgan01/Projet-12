package org.paniergarni.order.controller;

import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.paniergarni.order.business.OrderBusiness;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.entities.WrapperOrderProductDTO;
import org.paniergarni.order.exception.CriteriaException;
import org.paniergarni.order.exception.OrderException;
import org.paniergarni.order.dao.specification.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
@Api( description="API de gestion des commandes")
@RestController
public class OrderController {

    @Autowired
    private OrderBusiness orderBusiness;

    @ApiOperation(value = "Récupération d'une commande via son ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 409, message = "ID commande incorrect"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/adminRole/order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable(name = "id") Long id) throws OrderException {

        Order order = orderBusiness.getOrder(id);

        return ResponseEntity.ok().body(order);
    }

    @ApiOperation(value = "Récupération d'une commande via son ID et le userName d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 409, message = "ID commande incorrect, userName incorrect"),
            @ApiResponse(code = 500, message = "Erreur interne"),
            @ApiResponse(code = 503, message = "Service account indisponible")
    })
    @GetMapping(value = "/userRole/order/{id}/{userName}")
    public ResponseEntity<?> getOrderById(@PathVariable(name = "id") Long id, @PathVariable(name = "userName") String userName) throws OrderException, FeignException {

        Order order = orderBusiness.getOrder(id, userName);

        return ResponseEntity.ok().body(order);
    }

    @ApiOperation(value = "Recherche global des commandes d'un utilisateur via son userName, le numéro de la page, sa taille et une liste de critère")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 409, message = "UserName incorrect"),
            @ApiResponse(code = 406, message = "Critère incorrect"),
            @ApiResponse(code = 412, message = "Erreur du JSON"),
            @ApiResponse(code = 500, message = "Erreur interne"),
            @ApiResponse(code = 503, message = "Service account indisponible")
    })
    @PostMapping(value = "/userRole/orders/{userName}/{page}/{size}")
    public ResponseEntity<?> getOrdersByUserName(@PathVariable(name = "userName") String userName,
                                                 @PathVariable(name = "page") int page,
                                                 @PathVariable(name = "size") int size,
                                                 @RequestBody(required=false) List<SearchCriteria> searchCriteriaList) throws CriteriaException, FeignException {

        Page<Order> orders = orderBusiness.searchOrder(userName, page, size, searchCriteriaList);

        return ResponseEntity.ok().body(orders);
    }

    @ApiOperation(value = "Recherche global des commandes d'un utilisateur via le numéro de la page, sa taille et une liste de critère")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 406, message = "Critère incorrect"),
            @ApiResponse(code = 412, message = "Erreur du JSON"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PostMapping(value = "/adminRole/orders/{page}/{size}")
    public ResponseEntity<?> getOrders( @PathVariable(name = "page") int page,
                                        @PathVariable(name = "size") int size,
                                        @RequestBody(required=false) List<SearchCriteria> searchCriteriaList) throws CriteriaException {

        Page<Order> orders = orderBusiness.searchOrder(page, size, searchCriteriaList);

        return ResponseEntity.ok().body(orders);
    }

    @ApiOperation(value = "Recherche des commandes avec pour date de réception la date actuel")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/adminRole/receptionOrders")
    public ResponseEntity<?> getListReceptionOrder() {

        List<Order> orders = orderBusiness.getListOrderReception();

        return ResponseEntity.ok().body(orders);
    }

    @ApiOperation(value = "Recherche des commandes avec retard de réception")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/adminRole/lateOrders")
    public ResponseEntity<?> getListLateOrder() {

        List<Order> orders = orderBusiness.getListOrderLate();

        return ResponseEntity.ok().body(orders);
    }

    @ApiOperation(value = "Recherche des dates disponibles pour réception")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/userRole/listDateReception")
    public ResponseEntity<?> getListDateReception() {

        List<Date> dateList = orderBusiness.getListDateReception();

        return ResponseEntity.ok().body(dateList);
    }

    @ApiOperation(value = "Indique le nombre de jour maximal (hors jours de fermeture) pour réception")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/public/maxDaysReception")
    public ResponseEntity<?> getMaxDaysReception() {

        int a = orderBusiness.getMaxDaysReception();

        return ResponseEntity.ok().body(a);
    }

    @ApiOperation(value = "Indique le nombre d'heures maximal pour annulation d'une commande")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/public/maxHoursCancelOrder")
    public ResponseEntity<?> getMaxHoursCancelOrder() {

        int a = orderBusiness.getMaxHoursCancelOrder();

        return ResponseEntity.ok().body(a);
    }

    @ApiOperation(value = "Création d'une commande")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la création"),
            @ApiResponse(code = 409, message = "Produit indisponible, réception incorrect, entitée incorrect"),
            @ApiResponse(code = 412, message = "Erreur du JSON"),
            @ApiResponse(code = 500, message = "Erreur interne"),
            @ApiResponse(code = 503, message = "Service account indisponible")
    })
    @PostMapping(value = "/userRole/order/{userName}/{reception}")
    public ResponseEntity<?> createOrder(@PathVariable(name = "userName") String userName,
                                         @PathVariable(name = "reception") Long reception,
                                         @RequestBody @Valid WrapperOrderProductDTO wrapperOrderProductDTO) throws OrderException {

        Order order = orderBusiness.createOrder(wrapperOrderProductDTO, userName, reception);

        return ResponseEntity.ok().body(order);
    }

    @ApiOperation(value = "Annulation d'une commande")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de l'annulation"),
            @ApiResponse(code = 409, message = "ID ou userName incorrect, Commande déjà annulée, délais d'annulation dépassé"),
            @ApiResponse(code = 500, message = "Erreur interne"),
            @ApiResponse(code = 503, message = "Service account indisponible")
    })
    @PutMapping(value = "/userRole/cancelOrder/{id}/{userName}")
    public ResponseEntity<?> cancelOrder(@PathVariable(name = "id") Long id,
                                         @PathVariable(name = "userName") String userName) throws OrderException, FeignException {

        Order order = orderBusiness.cancelOrder(id, userName);

        return ResponseEntity.ok().body(order);
    }

    @ApiOperation(value = "Annulation d'une commande par administrateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de l'annulation"),
            @ApiResponse(code = 409, message = "ID incorrect, Commande déjà annulée"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/adminRole/cancelOrder/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable(name = "id") Long id) throws OrderException {

        Order order = orderBusiness.cancelOrder(id);

        return ResponseEntity.ok().body(order);
    }

    @ApiOperation(value = "Paiement d'une commande par administrateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès du paiement"),
            @ApiResponse(code = 409, message = "ID incorrect, Commande déjà payée"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/adminRole/paidOrder/{id}")
    public ResponseEntity<?> paidOrder(@PathVariable(name = "id") Long id) throws OrderException {

        orderBusiness.paidOrder(id);

        return ResponseEntity.ok().body(null);
    }
}
