<?php

namespace App\Controller;

use App\Entity\Cart;
use App\Repository\CartRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\JsonResponse;

#[Route('/cart')]
class CartController extends AbstractController
{
    private $entityManager;

    public function __construct(EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
    }

    #[Route(name: 'app_cart_index', methods: ['GET'])]
    public function index(CartRepository $cartRepository): Response
    {
        $carts = $cartRepository->findAll();
        return $this->render('cart/index.html.twig', [
            'carts' => $carts,
        ]);
    }

    #[Route('/new', name: 'app_cart_new', methods: ['GET'])]
    public function new(): Response
    {
        $cart = new Cart();
        $this->entityManager->persist($cart);
        $this->entityManager->flush();

        return $this->redirectToRoute('app_cart_index',[],  Response::HTTP_SEE_OTHER);
    }

    #[Route('/{id}', name: 'app_cart_show', methods: ['GET'])]
    public function show(Cart $cart): Response
    {
        return $this->render('cart/show.html.twig', [
            'cart' => $cart,
        ]);
    }

    #[Route('/{id}/delete', name: 'app_cart_delete', methods: ['POST'])]
    public function delete(Request $request, Cart $cart, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$cart->getId(), $request->request->get('_token'))) {
            $entityManager->remove($cart);
            $entityManager->flush();
            return $this->redirectToRoute('app_cart_index', [], Response::HTTP_SEE_OTHER);
        }

        return new JsonResponse(['error' => 'Invalid CSRF token'], Response::HTTP_FORBIDDEN);
    }
}